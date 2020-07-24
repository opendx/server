package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.agent.AgentClient;
import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.action.Step;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.model.dto.ActionTreeNode;
import com.daxiang.model.vo.ActionVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.dao.ActionDao;
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

    @Transactional
    public Response resetBasicAction(List<Action> actions) {
        if (CollectionUtils.isEmpty(actions)) {
            return Response.success();
        }

        // 删除基础action
        ActionExample example = new ActionExample();
        example.createCriteria().andTypeEqualTo(Action.TYPE_BASE);
        actionMapper.deleteByExample(example);

        actionDao.insertBasicActions(actions);

        return Response.success();
    }

    public Response delete(Integer actionId) {
        if (actionId == null) {
            return Response.fail("actionId不能为空");
        }

        checkAction(actionId);

        int deleteRow = actionMapper.deleteByPrimaryKey(actionId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后重试");
    }

    public Response update(Action action) {
        checkStepsNotContainsSelf(action);
        checkActionImportsNotContainsSelf(action);
        checkDepensNotContainsSelf(action);

        // action状态变为草稿或禁用
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

    public Response list(Action action, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
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
            return new ArrayList<>();
        }

        List<Integer> creatorAndUpdatorUids = actions.stream()
                .flatMap(action -> Stream.of(action.getCreatorUid(), action.getUpdatorUid()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorAndUpdatorUids);

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

    private List<Action> selectByAction(Action action) {
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

    public Response cascader(Integer projectId, Integer platform, Integer type) {
        if (projectId == null || platform == null) {
            return Response.fail("projectId || platform不能为空");
        }

        List<ActionTreeNode> tree = new ArrayList<>();

        // type可以为空
        List<Action> actions = actionDao.selectPublishedCascaderData(projectId, platform, type);
        if (CollectionUtils.isEmpty(actions)) {
            return Response.success(tree);
        }

        List<Category> categories = categoryService.getCategoriesWithProjectIdIsNullOrProjectIdEqualsTo(projectId);
        // categoryId : category
        Map<Integer, Category> categoryMap = categoryService.categoriesToMap(categories);

        // actionType : node
        Map<Integer, ActionTreeNode> rMap = new HashMap<>();
        // categoryId : node
        Map<Integer, ActionTreeNode> cMap = new HashMap<>();

        for (Action action : actions) {
            // 创建根节点加入到tree，如果没创建过
            Integer actionType = action.getType();
            ActionTreeNode root = rMap.get(actionType);
            if (root == null) {
                root = new ActionTreeNode();
                root.setName(getActionTypeName(actionType));
                root.setChildren(new ArrayList<>());
                rMap.put(actionType, root);
                tree.add(root);
            }

            Integer actionCid = action.getCategoryId();
            if (actionCid == null) {
                // 无分类的action，放入相应根节点
                root.getChildren().add(ActionTreeNode.create(action));
            } else {
                // action对应的分类节点
                ActionTreeNode cNode = cMap.get(actionCid);
                if (cNode != null) {
                    // action放入相应的分类
                    cNode.getChildren().add(ActionTreeNode.create(action));
                } else {
                    cNode = new ActionTreeNode();
                    Category category = categoryMap.get(actionCid);
                    cNode.setName(category.getName());
                    List<ActionTreeNode> children = new ArrayList<>();
                    // action放入相应分类
                    children.add(ActionTreeNode.create(action));
                    cNode.setChildren(children);
                    cMap.put(actionCid, cNode);

                    Integer pid = category.getParentId();
                    while (pid != null && pid > 0) {
                        Category parent = categoryMap.get(pid);
                        ActionTreeNode cpNode = cMap.get(pid);
                        if (cpNode == null) {
                            cpNode = new ActionTreeNode();
                            cpNode.setName(parent.getName());
                            cpNode.setChildren(new ArrayList<>());
                            cMap.put(pid, cpNode);
                        }
                        cpNode.getChildren().add(cNode);
                        cNode = cpNode;
                        pid = parent.getParentId();
                    }

                    root.getChildren().add(cNode);
                }
            }
        }

        return Response.success(tree);
    }

    private String getActionTypeName(Integer actionType) {
        switch (actionType) {
            case Action.TYPE_BASE:
                return "基础组件";
            case Action.TYPE_ENCAPSULATION:
                return "封装组件";
            case Action.TYPE_TESTCASE:
                return "测试用例";
            default:
                throw new BusinessException("unknow action type: " + actionType);
        }
    }

    public Response debug(ActionDebugRequest actionDebugRequest) {
        Action action = actionDebugRequest.getAction();
        action.setId(0);
        action.setDepends(new ArrayList<>());

        boolean anyEnabledStep = action.getSteps().stream()
                .anyMatch(step -> step.getStatus() == Step.ENABLE_STATUS);
        if (!anyEnabledStep) {
            return Response.fail("至少选择一个启用的步骤");
        }

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

    public Action getActionById(Integer actioniId) {
        return actionMapper.selectByPrimaryKey(actioniId);
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

    /**
     * 依赖用例不包含自身
     *
     * @param action
     */
    private void checkDepensNotContainsSelf(Action action) {
        List<Integer> depends = action.getDepends();
        if (!CollectionUtils.isEmpty(depends) && depends.contains(action.getId())) {
            throw new BusinessException("依赖用例不能包含自身");
        }
    }

    /**
     * 检查步骤不包含自身，防止出现死循环
     *
     * @param action
     */
    private void checkStepsNotContainsSelf(Action action) {
        List<Integer> stepActionIds = action.getSteps().stream()
                .map(Step::getActionId).collect(Collectors.toList());
        if (stepActionIds.contains(action.getId())) {
            throw new BusinessException("步骤不能包含自身");
        }
    }

    /**
     * 导入Action不能包含自身
     *
     * @param action
     */
    private void checkActionImportsNotContainsSelf(Action action) {
        List<Integer> actionImports = action.getActionImports();
        if (!CollectionUtils.isEmpty(actionImports) && actionImports.contains(action.getId())) {
            throw new BusinessException("导入Action不能包含自身");
        }
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
        List<TestPlan> testPlans = testPlanService.getTestPlansByActionId(actionId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String testPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new BusinessException("testPlans: " + testPlanNames + ", 正在使用此action");
        }

        // 检查action是否被testSuite使用
        List<TestSuite> testSuites = testSuiteService.getTestSuitesByActionId(actionId);
        if (!CollectionUtils.isEmpty(testSuites)) {
            String testSuiteNames = testSuites.stream().map(TestSuite::getName).collect(Collectors.joining("、"));
            throw new BusinessException("testSuites: " + testSuiteNames + ", 正在使用此action");
        }
    }

    public List<Action> getActionsByLocalVarsEnvironmentId(Integer envId) {
        if (envId == null) {
            return new ArrayList<>();
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
            return new ArrayList<>();
        }

        Action query = new Action();
        query.setPageId(pageId);
        return selectByAction(query);
    }

}
