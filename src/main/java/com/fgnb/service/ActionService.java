package com.fgnb.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fgnb.agent.AgentApi;
import com.fgnb.dao.ActionDao;
import com.fgnb.mbg.mapper.ActionMapper;
import com.fgnb.mbg.mapper.GlobalVarMapper;
import com.fgnb.mbg.mapper.ProjectMapper;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class ActionService extends BaseService {

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private GlobalVarMapper globalVarMapper;
    @Autowired
    private AgentApi agentApi;

    /**
     * 添加action
     *
     * @param action
     * @return
     */
    public Response add(Action action) {
        if (action.getType() == Action.TYPE_TESTCASE) {
            //测试用例 必须要有分类
            if (action.getCategoryId() == null) {
                return Response.fail("测试用例分类不能为空");
            }
        }

        action.setCreatorUid(getUid());
        action.setCreateTime(new Date());

        try {
            int insertRow = actionMapper.insertSelective(action);
            if (insertRow != 1) {
                return Response.fail("添加失败，稍后刷新重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success(action);
    }

    /**
     * 删除action
     *
     * @param actionId
     * @return
     */
    public Response delete(Integer actionId) {
        if (actionId == null) {
            return Response.fail("actionId不能为空");
        }

        List<Action> actions = actionDao.selectByStepActionId(actionId);
        if (!CollectionUtils.isEmpty(actions)) {
            //正在使用该action的actionNames
            String usingActionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            return Response.fail(usingActionNames + "正在使用此action，无法删除");
        }

        int deleteRow = actionMapper.deleteByPrimaryKey(actionId);
        if (deleteRow != 1) {
            return Response.fail("删除失败，请稍后重试");
        }
        return Response.success("删除成功");
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

        action.setUpdateTime(new Date());
        action.setUpdatorUid(getUid());

        if (action.getHasReturnValue() == Action.NO_RETURN_VALUE) {
            //如果returnValue是null，无法保存，重新设置为""
            action.setReturnValue("");
        }

        //由于action的参数，可能被其他步骤使用，类似于方法的参数改动，其他调用该方法的地方都要改动
        //为了安全起见，不更新ActionParam。设置null，插入的时候不会update这个值
        action.setParams(null);

        try {
            int updateRow = actionMapper.updateByPrimaryKeySelective(action);
            if (updateRow != 1) {
                return Response.fail("更新失败，请稍后重试!");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新成功");
    }

    /**
     * 查询action
     *
     * @param action
     * @param pageRequest
     * @return
     */
    public Response list(Action action, PageRequest pageRequest) {
        boolean needPageing = pageRequest.needPaging();
        //需要分页
        if (needPageing) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }
        List<ActionVo> actionVos = actionDao.selectByAction(action);
        if (needPageing) {
            return Response.success(Page.convert(new PageInfo(actionVos)));
        }
        return Response.success(actionVos);
    }

    /**
     * action步骤选择action，可供选择的action
     *
     * @param projectId
     * @return
     */
    public Response findSelectableActions(Integer projectId) {
        if (projectId == null) {
            return Response.fail("projectId不能为空");
        }
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project == null) {
            return Response.fail("项目不存在");
        }
        ActionExample actionExample = new ActionExample();

        //同一个项目下自定义action
        ActionExample.Criteria criteria1 = actionExample.createCriteria();
        criteria1.andProjectIdEqualTo(projectId).andTypeEqualTo(Action.TYPE_CUSTOM);

        //所有项目公用的基础action
        ActionExample.Criteria criteria2 = actionExample.createCriteria();
        criteria2.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andProjectTypeIsNull();

        //同一项目类型的基础action
        ActionExample.Criteria criteria3 = actionExample.createCriteria();
        criteria3.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andProjectTypeEqualTo(project.getType());

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

        //没保存过的action设置个默认的actionId
        if (action.getId() == null) {
            action.setId(0);
        }

        //构建action树
        new ActionTreeBuilder(actionMapper).build(Arrays.asList(action));

        //该项目下的全局变量
        GlobalVarExample globalVarExample = new GlobalVarExample();
        globalVarExample.createCriteria().andProjectIdEqualTo(action.getProjectId());
        List<GlobalVar> globalVars = globalVarMapper.selectByExample(globalVarExample);

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

    public List<Action> findByIds(List<Integer> actionIds) {
        if (CollectionUtils.isEmpty(actionIds)) {
            return new ArrayList<>();
        }

        ActionExample actionExample = new ActionExample();
        actionExample.createCriteria().andIdIn(actionIds);

        return actionMapper.selectByExampleWithBLOBs(actionExample);
    }

}
