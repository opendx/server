package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.agent.AgentClient;
import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.action.LocalVar;
import com.daxiang.model.action.Step;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.model.vo.ActionCascaderVo;
import com.daxiang.model.vo.ActionVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.dao.ActionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class ActionService {

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private GlobalVarService globalVarService;
    @Autowired
    private AgentClient agentClient;
    @Autowired
    private TestSuiteService testSuiteService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private PageService pageService;
    @Autowired
    private UserService userService;
    @Autowired
    private EnvironmentService environmentService;

    public Response add(Action action) {
        action.setCreatorUid(SecurityUtil.getCurrentUserId());
        action.setCreateTime(new Date());

        int insertRow;
        try {
            insertRow = actionMapper.insertSelective(action);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加action成功") : Response.fail("添加action失败，请稍后重试");
    }

    public Response delete(Integer actionId) {
        if (actionId == null) {
            return Response.fail("actionId不能为空");
        }

        checkAction(actionId);

        int deleteRow = actionMapper.deleteByPrimaryKey(actionId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后重试");
    }

    /**
     * 更新action
     *
     * @param action
     * @return
     */
    public Response update(Action action) {
        // 用例依赖不能依赖自己
        checkDepensNotSelf(action);

        // action状态变为草稿或者禁用
        if (action.getState() == Action.DRAFT_STATE || action.getState() == Action.DISABLE_STATE) {
            checkAction(action.getId());
        }

        action.setUpdateTime(new Date());
        action.setUpdatorUid(SecurityUtil.getCurrentUserId());

        int updateRow;
        try {
            updateRow = actionMapper.updateByPrimaryKeyWithBLOBs(action);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新Action成功") : Response.fail("更新Action失败，请稍后重试");
    }

    private void checkDepensNotSelf(Action action) {
        List<Integer> depends = action.getDepends();
        if (!CollectionUtils.isEmpty(depends) && depends.contains(action.getId())) {
            throw new BusinessException("依赖用例不能包含自身");
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
        List<ActionVo> actionVos = convertActionsToActionVos(actions);

        if (needPaging) {
            long total = Page.getTotal(actions);
            return Response.success(Page.build(actionVos, total));
        } else {
            return Response.success(actionVos);
        }
    }

    private List<ActionVo> convertActionsToActionVos(List<Action> actions) {
        if (CollectionUtils.isEmpty(actions)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> creatorAndUpdatorUids = actions.stream()
                .flatMap(action -> Stream.of(action.getCreatorUid(), action.getUpdatorUid()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByUserIds(creatorAndUpdatorUids);

        return actions.stream().map(action -> {
            ActionVo actionVo = new ActionVo();
            BeanUtils.copyProperties(action, actionVo);

            if (action.getCreatorUid() != null) {
                User user = userMap.get(action.getCreatorUid());
                if (user != null) {
                    actionVo.setCreatorNickName(user.getNickName());
                }
            }

            if (action.getUpdatorUid() != null) {
                User user = userMap.get(action.getUpdatorUid());
                if (user != null) {
                    actionVo.setUpdatorNickName(user.getNickName());
                }
            }

            return actionVo;
        }).collect(Collectors.toList());
    }

    public List<Action> selectByAction(Action action) {
        ActionExample example = new ActionExample();
        ActionExample.Criteria criteria = example.createCriteria();

        if (action != null) {
            if (action.getId() != null) {
                criteria.andIdEqualTo(action.getId());
            }
            if (!StringUtils.isEmpty(action.getName())) {
                criteria.andNameLike("%" + action.getName() + "%");
            }
            if (action.getProjectId() != null) {
                criteria.andProjectIdEqualTo(action.getProjectId());
            }
            if (action.getType() != null) {
                criteria.andTypeEqualTo(action.getType());
            }
            if (action.getPageId() != null) {
                criteria.andPageIdEqualTo(action.getPageId());
            }
            if (action.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(action.getCategoryId());
            }
            if (action.getState() != null) {
                criteria.andStateEqualTo(action.getState());
            }
        }
        example.setOrderByClause("create_time desc");

        return actionMapper.selectByExampleWithBLOBs(example);
    }

    public Response cascader(Integer projectId, Integer platform) {
        if (projectId == null || platform == null) {
            return Response.fail("projectId || platform不能为空");
        }

        // 已发布的actions
        List<Action> actions = actionDao.selectByProjectIdAndPlatform(projectId, platform).stream()
                .filter(action -> action.getState() == Action.RELEASE_STATE)
                .collect(Collectors.toList());

        // 分类
        List<Integer> categoryIds = actions.stream()
                .map(Action::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, Category> categoryMap = categoryService.getCategoryMapByCategoryIds(categoryIds);

        List<ActionCascaderVo> result = new ArrayList<>();

        actions.stream()
                .collect(Collectors.groupingBy(Action::getType)) // 按照类型分组
                .forEach((type, actionList) -> {
                    ActionCascaderVo root = new ActionCascaderVo();
                    root.setName(type == Action.TYPE_BASE ? "基础组件" : type == Action.TYPE_TESTCASE ? "测试用例" : "封装组件");
                    root.setChildren(getChildren(categoryMap, actionList));
                    result.add(root);
                });

        return Response.success(result);
    }

    private List<ActionCascaderVo> getChildren(Map<Integer, Category> categoryMap, List<Action> actionList) {
        // 按照是否有分类分区
        Map<Boolean, List<Action>> actionsMap = actionList.stream()
                .collect(Collectors.partitioningBy(action -> Objects.nonNull(action.getCategoryId())));

        List<ActionCascaderVo> result = new ArrayList<>();

        // 有分类的action 按照分类分组
        Map<Integer, List<Action>> actionsWithCategoryMap = actionsMap.get(true).stream()
                .collect(Collectors.groupingBy(Action::getCategoryId));

        actionsWithCategoryMap.forEach((categoryId, actionsWithCategory) -> {
            ActionCascaderVo actionCascaderVo = new ActionCascaderVo();
            actionCascaderVo.setName(categoryMap.get(categoryId).getName());

            List<ActionCascaderVo> children = actionsWithCategory.stream()
                    .map(ActionCascaderVo::convert).collect(Collectors.toList());
            actionCascaderVo.setChildren(children);

            result.add(actionCascaderVo);
        });

        // 无分类的actions
        List<ActionCascaderVo> actionWithoutCategoryCascaderVos = actionsMap.get(false).stream()
                .map(ActionCascaderVo::convert).collect(Collectors.toList());

        result.addAll(actionWithoutCategoryCascaderVos);
        return result;
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
        Integer env = debugInfo.getEnv();

        // 没保存过的action设置个默认的actionId
        if (action.getId() == null) {
            action.setId(0);
        }

        boolean anyEnabledStep = action.getSteps().stream().anyMatch(step -> step.getStatus() == Step.ENABLE_STATUS);
        if (!anyEnabledStep) {
            return Response.fail("至少选择一个启用的步骤");
        }

        // 根据环境处理action局部变量
        List<LocalVar> localVars = action.getLocalVars();
        if (!CollectionUtils.isEmpty(localVars)) {
            localVars.forEach(localVar -> {
                String value = environmentService.getValueInEnvironmentValues(localVar.getEnvironmentValues(), env);
                localVar.setValue(value);
            });
        }

        // 构建action树
        buildActionTree(Arrays.asList(action));

        // 该项目下的全局变量
        GlobalVar query = new GlobalVar();
        query.setProjectId(action.getProjectId());
        List<GlobalVar> globalVars = globalVarService.selectByGlobalVar(query);

        // 根据环境处理全局变量
        if (!CollectionUtils.isEmpty(globalVars)) {
            globalVars.forEach(globalVar -> {
                String value = environmentService.getValueInEnvironmentValues(globalVar.getEnvironmentValues(), env);
                globalVar.setValue(value);
            });
        }

        // 该项目下的Pages
        List<com.daxiang.mbg.po.Page> pages = pageService.findByProjectIdWithoutWindowHierarchy(action.getProjectId());

        JSONObject requestBody = new JSONObject();
        requestBody.put("platform", debugInfo.getPlatform());
        requestBody.put("action", action);
        requestBody.put("globalVars", globalVars);
        requestBody.put("pages", pages);
        requestBody.put("deviceId", debugInfo.getDeviceId());

        // 发送到agent执行
        return agentClient.debugAction(debugInfo.getAgentIp(), debugInfo.getAgentPort(), requestBody);
    }

    public Action selectByPrimaryKey(Integer actioniId) {
        return actionMapper.selectByPrimaryKey(actioniId);
    }

    public List<Action> selectByPrimaryKeys(List<Integer> actionIds) {
        if (CollectionUtils.isEmpty(actionIds)) {
            return Collections.EMPTY_LIST;
        }

        ActionExample example = new ActionExample();
        ActionExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(actionIds);
        return actionMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 构建actionTree
     *
     * @param actions
     */
    public void buildActionTree(List<Action> actions) {
        new ActionTreeBuilder(actions, actionMapper).build();
    }

    /**
     * 检查action没有被action或testplan或testSuite使用
     *
     * @param actionId
     */
    private void checkAction(Integer actionId) {
        // 检查action是否被steps或depends或actionImports使用
        List<Action> actions = actionDao.selectByActionIdInStepsOrDependsOrActionImports(actionId);
        if (!CollectionUtils.isEmpty(actions)) {
            String actionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            throw new BusinessException("actions: " + actionNames + ", 正在使用此action");
        }

        // 检查action是否被testplan使用
        List<TestPlan> testPlans = testPlanService.findByActionId(actionId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String testPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new BusinessException("testPlans: " + testPlanNames + ", 正在使用此action");
        }

        // 检查action是否被testSuite使用
        List<TestSuite> testSuites = testSuiteService.findByActionId(actionId);
        if (!CollectionUtils.isEmpty(testSuites)) {
            String testSuiteNames = testSuites.stream().map(TestSuite::getName).collect(Collectors.joining("、"));
            throw new BusinessException("testSuites: " + testSuiteNames + ", 正在使用此action");
        }
    }

    public List<Action> selectByLocalVarsEnvironmentId(Integer envId) {
        if (envId == null) {
            return Collections.EMPTY_LIST;
        }
        return actionDao.selectByLocalVarsEnvironmentId(envId);
    }

}
