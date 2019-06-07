package com.fgnb.service;

import com.alibaba.fastjson.JSONObject;
import com.fgnb.exception.BusinessException;
import com.fgnb.model.UserCache;
import com.fgnb.model.action.LocalVar;
import com.fgnb.model.action.Param;
import com.fgnb.model.action.Step;
import com.github.pagehelper.PageHelper;
import com.fgnb.agent.AgentApi;
import com.fgnb.dao.ActionDao;
import com.fgnb.mbg.mapper.ActionMapper;
import com.fgnb.mbg.po.*;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.ActionVo;
import com.fgnb.model.request.ActionDebugRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class ActionService extends BaseService {

    private static final String GLOBAL_VAR_PREFIX = "${"; // 全局变量前缀
    private static final String ACTION_PARAM_PREFIX = "#{"; // 方法参数前缀
    private static final String LOCAL_VAR_PREFIX = "@{"; // 局部变量前缀
    private static final String COMMON_SUFFIX = "}";

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private GlobalVarService globalVarService;
    @Autowired
    private AgentApi agentApi;

    public Response add(Action action) {
        check(action);

        action.setCreatorUid(getUid());
        action.setCreateTime(new Date());

        int insertRow;
        try {
            insertRow = actionMapper.insertSelective(action);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        if (insertRow == 1) {
            return Response.success("添加action成功");
        } else {
            return Response.fail("添加action失败，请稍后重试");
        }
    }

    public Response delete(Integer actionId) {
        if (actionId == null) {
            return Response.fail("actionId不能为空");
        }

        // 检查action是否被其他step使用
        List<Action> actions = actionDao.selectByStepActionId(actionId);
        if (!CollectionUtils.isEmpty(actions)) {
            // 正在使用该action的actionNames
            String usingActionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            return Response.fail(usingActionNames + "正在使用此action，无法删除");
        }

        int deleteRow = actionMapper.deleteByPrimaryKey(actionId);
        if (deleteRow == 1) {
            return Response.success("删除成功");
        } else {
            return Response.fail("删除失败，请稍后重试");
        }
    }

    /**
     * 更新action
     *
     * @param action
     * @return
     */
    public Response update(Action action) {
        if (action.getId() == null) {
            return Response.fail("actionId不能为空");
        }

        check(action);

        action.setUpdateTime(new Date());
        action.setUpdatorUid(getUid());

        // 为了防止更新action影响到其他action，暂时在前端屏蔽了更新方法参数与返回值（即方法参数无法添加删除，方法参数名无法修改，返回值无法编辑）
        int updateRow;
        try {
            updateRow = actionMapper.updateByPrimaryKeyWithBLOBs(action);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        if (updateRow == 1) {
            return Response.success("更新Action成功");
        } else {
            return Response.fail("更新Action失败，请稍后重试");
        }
    }

    /**
     * 查询action
     *
     * @param action
     * @param pageRequest
     * @return
     */
    public Response list(Action action, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        // 需要分页
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Action> actions = selectByAction(action);
        List<ActionVo> actionVos = actions.stream()
                .map(a -> {
                    String creatorNickName = a.getCreatorUid() == null ? null : UserCache.getNickNameById(a.getCreatorUid());
                    String updatorNickName = a.getUpdatorUid() == null ? null : UserCache.getNickNameById(a.getUpdatorUid());
                    return ActionVo.convert(a, creatorNickName, updatorNickName);
                }).collect(Collectors.toList());
        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用actions计算total
            long total = Page.getTotal(actions);
            return Response.success(Page.build(actionVos, total));
        } else {
            return Response.success(actionVos);
        }
    }

    /**
     * 校验action是否符合规则，防止编译出错。部分已经用@Valid校验过
     *
     * @param action
     */
    private void check(Action action) {
        // 1. 检查返回值。可以是一个普通的字符串或方法参数或局部变量或全局变量。全局变量暂时不检查
        if (action.getHasReturnValue() == Action.HAS_RETURN_VALUE) {
            String returnValue = action.getReturnValue(); // 前端已校验过，此时返回值一定有值
            if (returnValue.startsWith(ACTION_PARAM_PREFIX) && returnValue.endsWith(COMMON_SUFFIX)) {
                // 返回值 -> 方法参数
                List<Param> params = action.getParams();
                if (CollectionUtils.isEmpty(params)) {
                    throw new BusinessException("返回值" + returnValue + "为方法参数，但方法参数为空");
                }
                long count = params.stream().filter(param -> returnValue.substring(2, returnValue.length() - 1).equals(param.getName())).count();
                if (count != 1) {
                    throw new BusinessException("返回值" + returnValue + "为方法参数，但与返回值匹配的方法参数的个数不为1");
                }
            } else if (returnValue.startsWith(LOCAL_VAR_PREFIX) && returnValue.endsWith(COMMON_SUFFIX)) {
                // 返回值 -> 局部变量
                List<LocalVar> localVars = action.getLocalVars();
                if (CollectionUtils.isEmpty(localVars)) {
                    throw new BusinessException("返回值" + returnValue + "为局部变量，但局部变量为空");
                }
                long count = localVars.stream().filter(localVar -> returnValue.substring(2, returnValue.length() - 1).equals(localVar.getName())).count();
                if (count != 1) {
                    throw new BusinessException("返回值" + returnValue + "为局部变量，但与返回值匹配的局部变量的个数不为1");
                }
            }
        }

        // 2. 检查赋值。赋值必须为局部变量，给方法参数或全部变量重新赋值，意义不大
        List<Step> steps = action.getSteps(); // @valid已校验steps不为空
        List<String> evaluations = steps.stream().filter(step -> !StringUtils.isEmpty(step.getEvaluation())).map(Step::getEvaluation).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(evaluations)) {
            List<LocalVar> localVars = action.getLocalVars();
            if (CollectionUtils.isEmpty(localVars)) {
                throw new BusinessException("赋值不为空，但局部变量为空");
            }
            for (String evaluation : evaluations) {
                if (!evaluation.startsWith(LOCAL_VAR_PREFIX) || !evaluation.endsWith(COMMON_SUFFIX)) {
                    throw new BusinessException(evaluation + "必须为局部变量");
                }
                long count = localVars.stream().filter(localVar -> evaluation.substring(2, evaluation.length() - 1).equals(localVar.getName())).count();
                if (count != 1) {
                    throw new BusinessException("与赋值" + evaluation + "匹配的局部变量的个数不为1");
                }
            }
        }
    }

    public List<Action> selectByAction(Action action) {
        if (action == null) {
            action = new Action();
        }

        ActionExample actionExample = new ActionExample();
        ActionExample.Criteria criteria = actionExample.createCriteria();

        if (action.getId() != null) {
            criteria.andIdEqualTo(action.getId());
        }
        if (action.getProjectId() != null) {
            criteria.andProjectIdEqualTo(action.getProjectId());
        }
        if (action.getType() != null) {
            criteria.andTypeEqualTo(action.getType());
        }
        if (action.getPlatform() != null) {
            criteria.andPlatformEqualTo(action.getPlatform());
        }
        if (action.getPageId() != null) {
            criteria.andPageIdEqualTo(action.getPageId());
        }
        if (action.getTestSuiteId() != null) {
            criteria.andTestSuiteIdEqualTo(action.getTestSuiteId());
        }

        actionExample.setOrderByClause("create_time desc");
        return actionMapper.selectByExampleWithBLOBs(actionExample);
    }

    /**
     * action步骤选择action，可供选择的action
     *
     * @param projectId
     * @return
     */
    public Response getSelectableActions(Integer projectId, Integer platform) {
        if (projectId == null || platform == null) {
            return Response.fail("projectId || platform不能为空");
        }

        ActionExample actionExample = new ActionExample();

        // 同一个项目下的action
        ActionExample.Criteria criteria1 = actionExample.createCriteria();
        criteria1.andProjectIdEqualTo(projectId);

        // 所有项目公用的基础action
        ActionExample.Criteria criteria2 = actionExample.createCriteria();
        criteria2.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andPlatformIsNull();

        // 所有项目公用的同一平台的基础action
        ActionExample.Criteria criteria3 = actionExample.createCriteria();
        criteria3.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andPlatformEqualTo(platform);

        actionExample.or(criteria2);
        actionExample.or(criteria3);
        actionExample.setOrderByClause("create_time desc");

        return Response.success(actionMapper.selectByExampleWithBLOBs(actionExample));
    }

    /**
     * 调试action
     *
     * @param actionDebugRequest
     * @return
     */
    public Response debug(ActionDebugRequest actionDebugRequest) {
        Action action = actionDebugRequest.getAction();
        ActionDebugRequest.DebugInfo debugInfo = actionDebugRequest.getDebugInfo();

        // 没保存过的action设置个默认的actionId
        if (action.getId() == null) {
            action.setId(0);
        }

        // 构建action树
        buildActionTree(Arrays.asList(action));

        // 该项目下的全局变量
        GlobalVar globalVar = new GlobalVar();
        globalVar.setProjectId(action.getProjectId());
        List<GlobalVar> globalVars = globalVarService.selectByGlobalVar(globalVar);

        JSONObject requestBody = new JSONObject();
        requestBody.put("action", action);
        requestBody.put("globalVars", globalVars);
        requestBody.put("deviceId", debugInfo.getDeviceId());
        requestBody.put("port", debugInfo.getPort());

        //发送到agent执行
        Response agentResponse = agentApi.debugAction(debugInfo.getAgentIp(), debugInfo.getAgentPort(), requestBody);
        if (agentResponse.isSuccess()) {
            return Response.success(agentResponse.getMsg());
        } else {
            return Response.fail(agentResponse.getMsg());
        }
    }

    /**
     * 查询测试集下的测试用例
     *
     * @param testSuiteIds
     * @return
     */
    public List<Action> findByTestSuitIds(List<Integer> testSuiteIds) {
        if (CollectionUtils.isEmpty(testSuiteIds)) {
            return new ArrayList<>();
        }
        ActionExample actionExample = new ActionExample();
        actionExample.createCriteria().andTestSuiteIdIn(testSuiteIds);
        return actionMapper.selectByExampleWithBLOBs(actionExample);
    }

    public Action selectByPrimaryKey(Integer actioniId) {
        return actionMapper.selectByPrimaryKey(actioniId);
    }

    /**
     * 构建actionTree
     *
     * @param actions
     */
    public void buildActionTree(List<Action> actions) {
        new ActionTreeBuilder(actionMapper).build(actions);
    }

    public List<Action> findByProjectIdAndGlobalVar(Integer projectId, String globalVarName) {
        return actionDao.selectByProjectIdAndGlobalVarName(projectId, GLOBAL_VAR_PREFIX + globalVarName + COMMON_SUFFIX);
    }

}