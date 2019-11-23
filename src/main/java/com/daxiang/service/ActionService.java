package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.agent.AgentApi;
import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.action.Step;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.model.vo.ActionCascaderVo;
import com.daxiang.model.vo.ActionVo;
import com.daxiang.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.daxiang.dao.ActionDao;
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
    private GlobalVarService globalVarService;
    @Autowired
    private AgentApi agentApi;
    @Autowired
    private TestSuiteService testSuiteService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TestPlanService testPlanService;

    public Response add(Action action) {
        action.setCreatorUid(getUid());
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
        checkActionIsNotUsingByActionStepsOrTestPlans(actionId);

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
        // action状态变为草稿或者禁用，需要检查该action没有被其他action steps或testplans使用
        if (action.getState() == Action.DRAFT_STATE || action.getState() == Action.DISABLE_STATE) {
            checkActionIsNotUsingByActionStepsOrTestPlans(action.getId());
        } else {
            if (action.getId() == null) {
                return Response.fail("actionId不能为空");
            }
        }

        action.setUpdateTime(new Date());
        action.setUpdatorUid(getUid());

        int updateRow;
        try {
            updateRow = actionMapper.updateByPrimaryKeyWithBLOBs(action);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新Action成功") : Response.fail("更新Action失败，请稍后重试");
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
        if (action.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(action.getCategoryId());
        }
        if (action.getState() != null) {
            criteria.andStateEqualTo(action.getState());
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

        List<Action> actions = actionMapper.selectByExampleWithBLOBs(actionExample).stream()
                .filter(action -> action.getState() == Action.RELEASE_STATE).collect(Collectors.toList());
        List<ActionCascaderVo> result = new ArrayList<>();

        Map<Integer, List<Action>> groupByActionTypeMap = actions.stream().collect(Collectors.groupingBy(Action::getType));
        groupByActionTypeMap.forEach((type, actionList) -> {
            ActionCascaderVo root = new ActionCascaderVo();
            root.setName(type == Action.TYPE_BASE ? "基础组件" : type == Action.TYPE_TESTCASE ? "测试用例" : "封装组件");
            List<ActionCascaderVo> rootChildren = new ArrayList<>();
            root.setChildren(rootChildren);
            if (type == Action.TYPE_TESTCASE) {
                handleTestcases(actionList, rootChildren);
            } else {
                handleNonTestcases(actionList, rootChildren);
            }
            result.add(root);
        });

        return Response.success(result);
    }

    private void handleNonTestcases(List<Action> actionList, List<ActionCascaderVo> rootChildren) {
        // 分类
        List<Integer> categoryIds = actionList.stream().filter(action -> Objects.nonNull(action.getCategoryId())).map(Action::getCategoryId).collect(Collectors.toList());
        List<Category> categories = categoryService.selectByPrimaryKeys(categoryIds).stream().collect(Collectors.toList());

        // 有分类的action
        categories.forEach(category -> {
            // 第二级
            ActionCascaderVo categoryCascaderVo = new ActionCascaderVo();
            categoryCascaderVo.setName(category.getName());
            // 有分类的actions
            List<ActionCascaderVo> actionCascaderVosInCategory = actionList.stream()
                    .filter(action -> action.getCategoryId() == category.getId())
                    .map(action -> ActionCascaderVo.convert(action)).collect(Collectors.toList());
            categoryCascaderVo.setChildren(actionCascaderVosInCategory);
            rootChildren.add(categoryCascaderVo);
        });

        // 没有分类的actions
        List<ActionCascaderVo> actionCascaderVosNotInCategory = actionList.stream()
                .filter(action -> Objects.isNull(action.getCategoryId()))
                .map(action -> ActionCascaderVo.convert(action)).collect(Collectors.toList());
        rootChildren.addAll(actionCascaderVosNotInCategory);
    }

    private void handleTestcases(List<Action> testcaseList, List<ActionCascaderVo> rootChildren) {
        // 测试集
        List<Integer> testSuiteIds = testcaseList.stream().filter(action -> Objects.nonNull(action.getTestSuiteId())).map(Action::getTestSuiteId).collect(Collectors.toList());
        List<TestSuite> testSuites = testSuiteService.selectByPrimaryKeys(testSuiteIds).stream().collect(Collectors.toList());

        // 有测试集的testcases
        testSuites.forEach(testSuite -> {
            // 第二级
            ActionCascaderVo testSuiteCascaderVo = new ActionCascaderVo();
            testSuiteCascaderVo.setName(testSuite.getName());
            // 有测试集的actions
            List<ActionCascaderVo> actionCascaderVosInTestSuite = testcaseList.stream()
                    .filter(action -> action.getTestSuiteId() == testSuite.getId())
                    .map(action -> ActionCascaderVo.convert(action)).collect(Collectors.toList());
            testSuiteCascaderVo.setChildren(actionCascaderVosInTestSuite);
            rootChildren.add(testSuiteCascaderVo);
        });

        // 没有测试集的testcases
        List<ActionCascaderVo> actionCascaderVosNotInTestSuite = testcaseList.stream()
                .filter(action -> Objects.isNull(action.getTestSuiteId()))
                .map(action -> ActionCascaderVo.convert(action)).collect(Collectors.toList());
        rootChildren.addAll(actionCascaderVosNotInTestSuite);
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

        List<Step> steps = action.getSteps();
        if (CollectionUtils.isEmpty(steps)) {
            return Response.fail("至少选择一个步骤");
        }

        long enabledStepCount = steps.stream().filter(step -> step.getStatus() == Step.ENABLE_STATUS).count();
        if (enabledStepCount == 0) {
            return Response.fail("至少选择一个启用的步骤");
        }

        for (Step step : steps) {
            if (step.getActionId() == null) {
                return Response.fail("步骤" + step.getNumber() + " action不能为空");
            }
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

        // 发送到agent执行
        return agentApi.debugAction(debugInfo.getAgentIp(), debugInfo.getAgentPort(), requestBody);
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

    /**
     * 检查action没有被action step或testplan使用
     * @param actionId
     */
    private void checkActionIsNotUsingByActionStepsOrTestPlans(Integer actionId) {
        if (actionId == null) {
            throw new BusinessException("actionId不能为空");
        }

        // 检查action是否被其他action step使用
        List<Action> actions = actionDao.selectByStepActionId(actionId);
        if (!CollectionUtils.isEmpty(actions)) {
            // 正在使用该action的actionNames
            String usingActionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            throw new BusinessException(usingActionNames + "正在使用此action");
        }

        // 检查action是否被testplan使用
        List<TestPlan> testPlans = testPlanService.findByActionId(actionId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String usingTestPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new BusinessException(usingTestPlanNames + "正在使用此action");
        }
    }

}