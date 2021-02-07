package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.agent.AgentClient;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.action.Step;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.model.dto.ActionTreeNode;
import com.daxiang.model.vo.ActionVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.dao.ActionDao;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final Map<Integer, String> ACTION_TYPE_MAP = ImmutableMap.of(
            Action.TYPE_BASE, "基础Action",
            Action.TYPE_ENCAPSULATION, "Action",
            Action.TYPE_TESTCASE, "测试用例"
    );

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ActionDao actionDao;
    @Lazy
    @Autowired
    private ActionProcessor actionProcessor;
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

    public void add(Action action) {
        action.setCreateTime(new Date());
        action.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = actionMapper.insertSelective(action);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(action.getName() + "已存在");
        }
    }

    @Transactional
    public void resetBasicAction(List<Action> actions) {
        if (CollectionUtils.isEmpty(actions)) {
            return;
        }

        // 删除基础action
        ActionExample example = new ActionExample();
        example.createCriteria().andTypeEqualTo(Action.TYPE_BASE);
        actionMapper.deleteByExample(example);

        actionDao.insertBasicActions(actions);
    }

    public void delete(Integer actionId) {
        checkAction(actionId);

        int deleteCount = actionMapper.deleteByPrimaryKey(actionId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(Action action) {
        checkNotContainsSelf(action);

        // action状态变为草稿或禁用
        if (action.getState() != Action.RELEASE_STATE) {
            checkAction(action.getId());
        }

        action.setUpdateTime(new Date());
        action.setUpdatorUid(SecurityUtil.getCurrentUserId());

        try {
            int updateCount = actionMapper.updateByPrimaryKeyWithBLOBs(action);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(action.getName() + "已存在");
        }
    }

    public PagedData<ActionVo> list(Action query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<ActionVo> actionVos = getActionVos(query, orderBy);
        return new PagedData<>(actionVos, page.getTotal());
    }

    private List<ActionVo> convertActionsToActionVos(List<Action> actions) {
        if (CollectionUtils.isEmpty(actions)) {
            return new ArrayList<>();
        }

        List<Integer> creatorAndUpdatorUids = actions.stream()
                .flatMap(action -> Stream.of(action.getCreatorUid(), action.getUpdatorUid()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorAndUpdatorUids);

        List<ActionVo> actionVos = actions.stream().map(action -> {
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

        return actionVos;
    }

    public List<ActionVo> getActionVos(Action query, String orderBy) {
        List<Action> actions = getActions(query, orderBy);
        return convertActionsToActionVos(actions);
    }

    public List<Action> getActions(Action query) {
        return getActions(query, null);
    }

    public List<Action> getActions(Action query, String orderBy) {
        ActionExample example = new ActionExample();

        if (query != null) {
            ActionExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameLike("%" + query.getName() + "%");
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
            if (query.getType() != null) {
                criteria.andTypeEqualTo(query.getType());
            }
            if (query.getPageId() != null) {
                criteria.andPageIdEqualTo(query.getPageId());
            }
            if (query.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(query.getCategoryId());
            }
            if (query.getState() != null) {
                criteria.andStateEqualTo(query.getState());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return actionMapper.selectByExampleWithBLOBs(example);
    }

    public List<ActionTreeNode> cascader(Integer projectId, Integer platform, Integer type) {
        if (projectId == null || platform == null) {
            throw new ServerException("projectId or platform不能为空");
        }

        List<ActionTreeNode> tree = new ArrayList<>();

        // type可以为空
        List<Action> actions = actionDao.selectPublishedCascaderData(projectId, platform, type);
        if (CollectionUtils.isEmpty(actions)) {
            return tree;
        }

        List<Category> categories = categoryService.getCategoriesWithProjectIdIsNullOrProjectIdEqualsTo(projectId);
        // categoryId : category
        Map<Integer, Category> categoryMap = categoryService.categoriesToMap(categories);

        // actionType : node
        Map<Integer, ActionTreeNode> actionTypeNodeMap = new HashMap<>();
        // categoryId : node
        Map<Integer, ActionTreeNode> categoryIdNodeMap = new HashMap<>();

        for (Action action : actions) {
            // 创建根节点加入到tree，如果没创建过
            Integer actionType = action.getType();
            ActionTreeNode root = actionTypeNodeMap.get(actionType);
            if (root == null) {
                root = new ActionTreeNode();
                root.setName(ACTION_TYPE_MAP.get(actionType));
                root.setChildren(new ArrayList<>());

                tree.add(root);
                actionTypeNodeMap.put(actionType, root);
            }

            ActionTreeNode actionNode = ActionTreeNode.create(action);
            Integer actionCid = action.getCategoryId();
            if (actionCid == null) {
                // 无分类的action，放入相应根节点
                root.getChildren().add(actionNode);
            } else {
                // action对应的分类节点
                ActionTreeNode cNode = categoryIdNodeMap.get(actionCid);
                if (cNode != null) {
                    // action放入相应的分类
                    cNode.getChildren().add(actionNode);
                } else {
                    Category category = categoryMap.get(actionCid);

                    cNode = new ActionTreeNode();
                    cNode.setName(category.getName());
                    cNode.setChildren(new ArrayList<>());
                    // action放入相应分类
                    cNode.getChildren().add(actionNode);
                    categoryIdNodeMap.put(actionCid, cNode);

                    boolean cNodeIsRootChildren = true;

                    Integer pCid = category.getParentId();
                    while (pCid != null && pCid > 0) {
                        ActionTreeNode pCNode = categoryIdNodeMap.get(pCid);
                        if (pCNode != null) {
                            pCNode.getChildren().add(cNode);
                            cNodeIsRootChildren = false;
                            break;
                        }

                        Category pCategory = categoryMap.get(pCid);

                        pCNode = new ActionTreeNode();
                        pCNode.setName(pCategory.getName());
                        pCNode.setChildren(new ArrayList<>());
                        pCNode.getChildren().add(cNode);
                        categoryIdNodeMap.put(pCid, pCNode);

                        cNode = pCNode;
                        pCid = pCategory.getParentId();
                    }

                    if (cNodeIsRootChildren) {
                        root.getChildren().add(cNode);
                    }
                }
            }
        }

        return tree;
    }

    public Response debug(ActionDebugRequest actionDebugRequest) {
        Action action = actionDebugRequest.getAction();

        boolean anyEnabledStep = action.getSteps() != null &&
                action.getSteps().stream().anyMatch(step -> step.getStatus() == Step.ENABLE_STATUS);

        if (!anyEnabledStep) {
            boolean anyEnabledSetUpStep = action.getSetUp() != null &&
                    action.getSetUp().stream().anyMatch(step -> step.getStatus() == Step.ENABLE_STATUS);
            if (!anyEnabledSetUpStep) {
                boolean anyEnabledTearDownStep = action.getTearDown() != null &&
                        action.getTearDown().stream().anyMatch(step -> step.getStatus() == Step.ENABLE_STATUS);
                if (!anyEnabledTearDownStep) {
                    return Response.fail("至少选择一个启用的步骤");
                }
            }
        }

        action.setId(0);
        action.setDepends(new ArrayList<>());

        ActionDebugRequest.DebugInfo debugInfo = actionDebugRequest.getDebugInfo();
        Integer projectId = action.getProjectId();
        Integer env = debugInfo.getEnv();

        actionProcessor.process(Arrays.asList(action), env);

        // 该项目下的全局变量
        List<GlobalVar> globalVars = globalVarService.getGlobalVarsByProjectIdAndEnv(projectId, env);
        // 该项目下的Pages
        List<com.daxiang.mbg.po.Page> pages = pageService.getPagesWithoutWindowHierarchyByProjectId(projectId);

        JSONObject requestBody = new JSONObject();
        requestBody.put("platform", debugInfo.getPlatform());
        requestBody.put("action", action);
        requestBody.put("globalVars", globalVars);
        requestBody.put("pages", pages);
        requestBody.put("deviceId", debugInfo.getDeviceId());

        // 发送到agent执行
        return agentClient.debugAction(debugInfo.getAgentIp(), debugInfo.getAgentPort(), requestBody);
    }

    public Action getActionById(Integer actionId) {
        return actionMapper.selectByPrimaryKey(actionId);
    }

    public List<Action> getActionsByIds(List<Integer> actionIds) {
        if (CollectionUtils.isEmpty(actionIds)) {
            return new ArrayList<>();
        }

        ActionExample example = new ActionExample();
        ActionExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(actionIds);
        return actionMapper.selectByExampleWithBLOBs(example);
    }

    private void checkNotContainsSelf(Action action) {
        List<Step> setUp = action.getSetUp();
        if (!CollectionUtils.isEmpty(setUp)) {
            List<Integer> setUpActionIds = setUp.stream()
                    .map(Step::getActionId).collect(Collectors.toList());
            if (setUpActionIds.contains(action.getId())) {
                throw new ServerException("setUp不能包含自身");
            }
        }

        List<Integer> stepActionIds = action.getSteps().stream()
                .map(Step::getActionId).collect(Collectors.toList());
        if (stepActionIds.contains(action.getId())) {
            throw new ServerException("步骤不能包含自身");
        }

        List<Step> tearDown = action.getTearDown();
        if (!CollectionUtils.isEmpty(tearDown)) {
            List<Integer> tearDownActionIds = tearDown.stream()
                    .map(Step::getActionId).collect(Collectors.toList());
            if (tearDownActionIds.contains(action.getId())) {
                throw new ServerException("tearDown不能包含自身");
            }
        }

        List<Integer> actionImports = action.getActionImports();
        if (!CollectionUtils.isEmpty(actionImports) && actionImports.contains(action.getId())) {
            throw new ServerException("导入Action不能包含自身");
        }

        List<Integer> depends = action.getDepends();
        if (!CollectionUtils.isEmpty(depends) && depends.contains(action.getId())) {
            throw new ServerException("依赖用例不能包含自身");
        }
    }

    /**
     * 检查action没有被action或testplan或testSuite使用
     *
     * @param actionId
     */
    private void checkAction(Integer actionId) {
        if (actionId == null) {
            throw new ServerException("actionId不能为空");
        }

        // 检查action是否被其他action使用
        List<Action> actions = actionDao.selectOtherActionsInUse(actionId);
        if (!CollectionUtils.isEmpty(actions)) {
            String actionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            throw new ServerException("actions: " + actionNames + ", 正在使用此action");
        }

        // 检查action是否被testplan使用
        List<TestPlan> testPlans = testPlanService.getTestPlansByActionId(actionId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String testPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new ServerException("testPlans: " + testPlanNames + ", 正在使用此action");
        }

        // 检查action是否被testSuite使用
        List<TestSuite> testSuites = testSuiteService.getTestSuitesByActionId(actionId);
        if (!CollectionUtils.isEmpty(testSuites)) {
            String testSuiteNames = testSuites.stream().map(TestSuite::getName).collect(Collectors.joining("、"));
            throw new ServerException("testSuites: " + testSuiteNames + ", 正在使用此action");
        }
    }

    public List<Action> getActionsByLocalVarsEnvironmentId(Integer envId) {
        if (envId == null) {
            throw new ServerException("envId不能为空");
        }
        return actionDao.selectByLocalVarsEnvironmentId(envId);
    }

    public List<Action> getActionsByCategoryIds(List<Integer> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return new ArrayList<>();
        }

        ActionExample example = new ActionExample();
        ActionExample.Criteria criteria = example.createCriteria();

        criteria.andCategoryIdIn(categoryIds);
        return actionMapper.selectByExample(example);
    }

    public List<Action> getActionsByPageId(Integer pageId) {
        if (pageId == null) {
            throw new ServerException("pageId不能为空");
        }

        Action query = new Action();
        query.setPageId(pageId);
        return getActions(query);
    }

}
