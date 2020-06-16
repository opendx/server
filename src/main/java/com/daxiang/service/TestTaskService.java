package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.mapper.TestTaskMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.action.LocalVar;
import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.model.vo.TestTaskVo;
import com.daxiang.model.vo.TestTaskSummary;
import com.github.pagehelper.PageHelper;
import com.daxiang.exception.BusinessException;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.dto.Testcase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jiangyitao.
 */
@Service
public class TestTaskService {

    @Autowired
    private TestTaskMapper testTaskMapper;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private DeviceTestTaskService deviceTestTaskService;
    @Autowired
    private GlobalVarService globalVarService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private PageService pageService;
    @Autowired
    private UserService userService;
    @Autowired
    private TestSuiteService testSuiteService;
    @Autowired
    private BrowserService browserService;
    @Autowired
    private MobileService mobileService;

    /**
     * 提交测试任务
     *
     * @return
     */
    @Transactional
    public Response commit(Integer testPlanId, Integer commitorUid) {
        if (testPlanId == null || commitorUid == null) {
            return Response.fail("testPlanId || commitorUid 不能为空");
        }

        TestPlan testPlan = testPlanService.getTestPlanById(testPlanId);
        if (testPlan == null) {
            return Response.fail("测试计划不存在");
        }

        // 待测试的测试用例
        List<Integer> testcaseIds = testSuiteService.getTestSuitesByIds(testPlan.getTestSuites()).stream()
                .flatMap(testSuite -> testSuite.getTestcases().stream())
                .distinct().collect(Collectors.toList());
        // 过滤出已发布的用例
        List<Action> testcases = actionService.getActionsByIds(testcaseIds).stream()
                .filter(action -> action.getState() == Action.RELEASE_STATE).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(testcases)) {
            return Response.fail("测试集内没有已发布的测试用例");
        }

        testcaseIds = testcases.stream().map(Action::getId).collect(Collectors.toList());
        // 检查testcase依赖的testcase是否存在
        for (Action testcase : testcases) {
            List<Integer> depends = testcase.getDepends();
            if (!CollectionUtils.isEmpty(depends) && !testcaseIds.containsAll(depends)) {
                return Response.fail("测试用例: " + testcase.getName() + ", 依赖的用例不在当前提测的所有用例中");
            }
        }

        // 前置后置action
        List<Integer> beforeAndAfterActionIds = Stream.of(testPlan.getBeforeClass(), testPlan.getBeforeMethod(), testPlan.getAfterClass(), testPlan.getAfterMethod())
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Integer, Action> beforeAndAfterActionMap = actionService.getActionsByIds(beforeAndAfterActionIds).stream()
                .collect(Collectors.toMap(Action::getId, a -> a));

        // 待处理的所有action
        List<Action> actions = new ArrayList<>(testcases);
        actions.addAll(beforeAndAfterActionMap.values());

        // 根据环境处理局部变量
        actions.forEach(action -> {
            List<LocalVar> localVars = action.getLocalVars();
            if (!CollectionUtils.isEmpty(localVars)) {
                localVars.forEach(localVar -> {
                    String value = environmentService.getValueInEnvironmentValues(localVar.getEnvironmentValues(), testPlan.getEnvironmentId());
                    localVar.setValue(value);
                });
            }
        });

        actionService.processActions(actions);

        // 保存测试任务
        TestTask testTask = saveTestTask(testPlan, commitorUid);

        // 同一项目下的全局变量
        List<GlobalVar> globalVars = globalVarService.getGlobalVarsByProjectId(testTask.getProjectId());

        // 根据环境处理全局变量
        if (!CollectionUtils.isEmpty(globalVars)) {
            globalVars.forEach(globalVar -> {
                String value = environmentService.getValueInEnvironmentValues(globalVar.getEnvironmentValues(), testPlan.getEnvironmentId());
                globalVar.setValue(value);
            });
        }

        // 该项目下的Pages
        List<com.daxiang.mbg.po.Page> pages = pageService.getPagesWithoutWindowHierarchyByProjectId(testTask.getProjectId());

        Project project = projectService.getProjectById(testTask.getProjectId());

        // 根据不同用例分发策略，给device分配用例
        Map<String, List<Action>> deviceTestcases = allocateTestcaseToDevice(testPlan.getDeviceIds(), testcases, testPlan.getRunMode());

        Map<String, JSONObject> deviceMap = new HashMap<>();
        Set<String> deviceIds = deviceTestcases.keySet();
        if (project.getPlatform() == Project.PC_WEB_PLATFORM) { // browser
            Map<String, Browser> browserMap = browserService.getBrowserMapByBrowserIds(deviceIds);
            browserMap.forEach((browserId, browser) -> deviceMap.put(browserId, (JSONObject) JSONObject.toJSON(browser)));
        } else { // mobile
            Map<String, Mobile> mobileMap = mobileService.getMobileMapByIds(deviceIds);
            mobileMap.forEach((mobileId, mobile) -> deviceMap.put(mobileId, (JSONObject) JSONObject.toJSON(mobile)));
        }

        // todo 批量保存
        deviceTestcases.forEach((deviceId, actionList) -> {
            DeviceTestTask deviceTestTask = new DeviceTestTask();
            deviceTestTask.setProjectId(testTask.getProjectId());
            deviceTestTask.setPlatform(project.getPlatform());
            deviceTestTask.setCapabilities(project.getCapabilities());
            deviceTestTask.setTestTaskId(testTask.getId());
            deviceTestTask.setTestPlan(testPlan);
            deviceTestTask.setDeviceId(deviceId);
            deviceTestTask.setDevice(deviceMap.get(deviceId));
            deviceTestTask.setGlobalVars(globalVars);
            deviceTestTask.setPages(pages);
            if (testPlan.getBeforeClass() != null) {
                deviceTestTask.setBeforeClass(beforeAndAfterActionMap.get(testPlan.getBeforeClass()));
            }
            if (testPlan.getBeforeMethod() != null) {
                deviceTestTask.setBeforeMethod(beforeAndAfterActionMap.get(testPlan.getBeforeMethod()));
            }
            if (testPlan.getAfterClass() != null) {
                deviceTestTask.setAfterClass(beforeAndAfterActionMap.get(testPlan.getAfterClass()));
            }
            if (testPlan.getAfterMethod() != null) {
                deviceTestTask.setAfterMethod(beforeAndAfterActionMap.get(testPlan.getAfterMethod()));
            }
            List<Testcase> cases = actionList.stream().map(action -> {
                Testcase testcase = new Testcase();
                BeanUtils.copyProperties(action, testcase);
                return testcase;
            }).collect(Collectors.toList());
            deviceTestTask.setTestcases(cases);
            deviceTestTask.setStatus(DeviceTestTask.UNSTART_STATUS);

            int insertRow = deviceTestTaskService.add(deviceTestTask);
            if (insertRow != 1) {
                throw new BusinessException(deviceId + "保存测试任务失败");
            }
        });

        return Response.success("提交测试成功");
    }

    /**
     * 给device分配测试用例
     *
     * @param deviceIds
     * @param testcases
     * @param runMode
     * @return
     */
    private Map<String, List<Action>> allocateTestcaseToDevice(List<String> deviceIds, List<Action> testcases, Integer runMode) {
        Map<String, List<Action>> result = new HashMap<>(); // deviceId : List<Action>

        if (runMode == TestPlan.RUN_MODE_COMPATIBLE) { // 兼容模式：所有device都运行同一份用例
            result = deviceIds.stream().collect(Collectors.toMap(deviceId -> deviceId, v -> testcases));
        } else if (runMode == TestPlan.RUN_MODE_EFFICIENCY) { // 高效模式：平均分配用例给device
            int i = 0; // 分配到第几个device
            for (Action testcase : testcases) {
                String deviceId = deviceIds.get(i);
                List<Action> actions = result.computeIfAbsent(deviceId, k -> new ArrayList<>());
                actions.add(testcase);
                i++;
                // 分配完最后一个device，再从第一个device开始分配
                if (i == deviceIds.size()) {
                    i = 0;
                }
            }
        }

        return result;
    }

    private TestTask saveTestTask(TestPlan testPlan, Integer commitorUid) {
        TestTask testTask = new TestTask();

        testTask.setProjectId(testPlan.getProjectId());
        testTask.setTestPlanId(testPlan.getId());
        testTask.setTestPlan(testPlan);
        testTask.setStatus(TestTask.UNFINISHED_STATUS);
        testTask.setCreatorUid(commitorUid);
        testTask.setCommitTime(new Date());

        int insertRow = testTaskMapper.insertSelective(testTask);
        if (insertRow == 1) {
            return testTask;
        } else {
            throw new BusinessException("保存TestTask失败");
        }
    }

    public Response list(TestTask testTask, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<TestTask> testTasks = selectByTestTask(testTask);
        List<TestTaskVo> testTaskVos = convertTestTasksToTestTaskVos(testTasks);

        if (needPaging) {
            long total = Page.getTotal(testTasks);
            return Response.success(Page.build(testTaskVos, total));
        } else {
            return Response.success(testTaskVos);
        }
    }

    private List<TestTaskVo> convertTestTasksToTestTaskVos(List<TestTask> testTasks) {
        if (CollectionUtils.isEmpty(testTasks)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = testTasks.stream()
                .map(TestTask::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        return testTasks.stream().map(testTask -> {
            TestTaskVo testTaskVo = new TestTaskVo();
            BeanUtils.copyProperties(testTask, testTaskVo);

            if (testTask.getCreatorUid() != null) {
                User user = userMap.get(testTask.getCreatorUid());
                if (user != null) {
                    testTaskVo.setCreatorNickName(user.getNickName());
                }
            }

            return testTaskVo;
        }).collect(Collectors.toList());
    }

    private List<TestTask> selectByTestTask(TestTask testTask) {
        TestTaskExample example = new TestTaskExample();
        TestTaskExample.Criteria criteria = example.createCriteria();

        if (testTask != null) {
            if (testTask.getId() != null) {
                criteria.andIdEqualTo(testTask.getId());
            }
            if (testTask.getProjectId() != null) {
                criteria.andProjectIdEqualTo(testTask.getProjectId());
            }
            if (testTask.getTestPlanId() != null) {
                criteria.andTestPlanIdEqualTo(testTask.getTestPlanId());
            }
            if (testTask.getStatus() != null) {
                criteria.andStatusEqualTo(testTask.getStatus());
            }
        }
        example.setOrderByClause("commit_time desc");

        return testTaskMapper.selectByExampleWithBLOBs(example);
    }

    public List<TestTask> getUnFinishedTestTasks() {
        TestTask testTask = new TestTask();
        testTask.setStatus(TestTask.UNFINISHED_STATUS);
        return selectByTestTask(testTask);
    }

    public int update(TestTask testTask) {
        return testTaskMapper.updateByPrimaryKeySelective(testTask);
    }

    public Response getTestTaskSummary(Integer testTaskId) {
        if (testTaskId == null) {
            return Response.fail("testTaskId不能为空");
        }

        TestTask testTask = testTaskMapper.selectByPrimaryKey(testTaskId);
        if (testTask == null) {
            return Response.fail("测试任务不存在");
        }

        Project project = projectService.getProjectById(testTask.getProjectId());
        if (project == null) {
            return Response.fail("项目不存在");
        }

        TestTaskSummary summary = new TestTaskSummary();
        BeanUtils.copyProperties(testTask, summary);
        summary.setPlatform(project.getPlatform());
        summary.setProjectName(project.getName());

        User user = userService.getUserById(testTask.getCreatorUid());
        if (user != null) {
            summary.setCommitorNickName(user.getNickName());
        }

        Integer passCaseCount = testTask.getPassCaseCount();
        Integer totalCaseCount = testTask.getPassCaseCount() + testTask.getFailCaseCount() + testTask.getSkipCaseCount();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2); // 精确到小数点2位

        String passPercent = numberFormat.format(passCaseCount.floatValue() * 100 / totalCaseCount) + "%";
        summary.setPassPercent(passPercent);

        if (testTask.getTestPlan() != null) {
            Integer environmentId = testTask.getTestPlan().getEnvironmentId();
            if (environmentId == EnvironmentValue.DEFAULT_ENVIRONMENT_ID) {
                summary.setEnvironmentName("默认");
            } else {
                Environment environment = environmentService.getEnvironmentById(environmentId);
                if (environment != null) {
                    summary.setEnvironmentName(environment.getName());
                }
            }
        }

        return Response.success(summary);
    }

    @Transactional
    public Response delete(Integer testTaskId) {
        if (testTaskId == null) {
            return Response.fail("testTaskId不能为空");
        }

        TestTask testTask = testTaskMapper.selectByPrimaryKey(testTaskId);
        if (testTask == null) {
            return Response.fail("testTask不存在");
        }

        List<DeviceTestTask> deviceTestTasks = deviceTestTaskService.getDeviceTestTasksByTestTaskId(testTaskId);

        if (!CollectionUtils.isEmpty(deviceTestTasks)) {
            List<DeviceTestTask> deviceTestTasksInRunning = deviceTestTasks.stream()
                    .filter(deviceTestTask -> deviceTestTask.getStatus() == DeviceTestTask.RUNNING_STATUS)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deviceTestTasksInRunning)) {
                // 有device还在运行，不让删除testTask
                String deviceIdsInRunning = deviceTestTasksInRunning.stream()
                        .map(DeviceTestTask::getDeviceId).collect(Collectors.joining("、"));
                return Response.fail(deviceIdsInRunning + "，正在运行，无法删除");
            } else {
                // 批量删除deviceTestTask
                List<Integer> deviceTestTaskIds = deviceTestTasks.stream().map(DeviceTestTask::getId).collect(Collectors.toList());
                int deleteRow = deviceTestTaskService.deleteBatch(deviceTestTaskIds);
                if (deleteRow != deviceTestTasks.size()) {
                    throw new BusinessException(String.format("删除deviceTestTask失败，deviceTestTasks: %d, deleteRow: %d", deviceTestTasks.size(), deleteRow));
                }
            }
        }

        // 删除testTask
        int deleteTestTaskRow = testTaskMapper.deleteByPrimaryKey(testTaskId);
        if (deleteTestTaskRow == 1) {
            return Response.success("删除成功");
        } else {
            throw new BusinessException("删除失败，请稍后重试");
        }
    }
}
