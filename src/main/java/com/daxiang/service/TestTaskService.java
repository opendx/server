package com.daxiang.service;

import com.daxiang.mbg.mapper.TestTaskMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.vo.TestTaskVo;
import com.daxiang.model.UserCache;
import com.daxiang.model.vo.TestTaskSummary;
import com.github.pagehelper.PageHelper;
import com.daxiang.exception.BusinessException;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.request.CommitTestTaskRequest;
import com.daxiang.model.vo.Testcase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
@Service
public class TestTaskService extends BaseService {

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

    /**
     * 提交测试任务
     *
     * @param commitTestTaskRequest
     * @return
     */
    @Transactional
    public Response commit(CommitTestTaskRequest commitTestTaskRequest) {
        TestTask testTask = saveTestTask(commitTestTaskRequest);
        TestPlan testPlan = testPlanService.selectByPrimaryKey(testTask.getTestPlanId());

        Action beforeClass = testPlan.getBeforeClass() != null ? actionService.selectByPrimaryKey(testPlan.getBeforeClass()) : null;
        Action beforeMethod = testPlan.getBeforeMethod() != null ? actionService.selectByPrimaryKey(testPlan.getBeforeMethod()) : null;
        Action afterClass = testPlan.getAfterClass() != null ? actionService.selectByPrimaryKey(testPlan.getAfterClass()) : null;
        Action afterMethod = testPlan.getAfterMethod() != null ? actionService.selectByPrimaryKey(testPlan.getAfterMethod()) : null;
        List<Action> testcases = actionService.findByTestSuitIds(testPlan.getTestSuites());

        List<Action> needBuildActions = new ArrayList<>(testcases);
        if (beforeClass != null) {
            needBuildActions.add(beforeClass);
        }
        if (beforeMethod != null) {
            needBuildActions.add(beforeMethod);
        }
        if (afterClass != null) {
            needBuildActions.add(afterClass);
        }
        if (afterMethod != null) {
            needBuildActions.add(afterMethod);
        }

        actionService.buildActionTree(needBuildActions);

        // 同一项目下的全局变量
        GlobalVar globalVar = new GlobalVar();
        globalVar.setProjectId(testTask.getProjectId());
        List<GlobalVar> globalVars = globalVarService.selectByGlobalVar(globalVar);

        //根据不同用例分发策略，给设备分配用例
        Map<String, List<Action>> deviceTestcases = allocateTestcaseToDevice(commitTestTaskRequest.getDeviceIds(), testcases, testTask.getRunMode());

        deviceTestcases.forEach((deviceId, actions) -> {
            DeviceTestTask deviceTestTask = new DeviceTestTask();
            deviceTestTask.setProjectId(testTask.getProjectId());
            deviceTestTask.setTestTaskId(testTask.getId());
            deviceTestTask.setTestTaskName(testTask.getName());
            deviceTestTask.setDeviceId(deviceId);
            deviceTestTask.setGlobalVars(globalVars);
            if (beforeClass != null) {
                deviceTestTask.setBeforeClass(beforeClass);
            }
            if (beforeMethod != null) {
                deviceTestTask.setBeforeMethod(beforeMethod);
            }
            if (afterClass != null) {
                deviceTestTask.setAfterClass(afterClass);
            }
            if (afterMethod != null) {
                deviceTestTask.setAfterMethod(afterMethod);
            }
            List<Testcase> cases = actions.stream().map(action -> {
                Testcase testcase = new Testcase();
                BeanUtils.copyProperties(action, testcase);
                return testcase;
            }).collect(Collectors.toList());
            deviceTestTask.setTestcases(cases);
            deviceTestTask.setStatus(DeviceTestTask.UNSTART_STATUS);

            int insertRow = deviceTestTaskService.insertSelective(deviceTestTask);
            if (insertRow != 1) {
                throw new BusinessException(deviceId + "保存测试任务失败");
            }
        });

        return Response.success("提交测试任务成功");
    }

    /**
     * 给设备分配测试用例
     *
     * @param deviceIds
     * @param testcases
     * @param runMode
     * @return
     */
    private Map<String, List<Action>> allocateTestcaseToDevice(List<String> deviceIds, List<Action> testcases, Integer runMode) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            throw new BusinessException("设备不能为空");
        }
        if (CollectionUtils.isEmpty(testcases)) {
            throw new BusinessException("测试用例不能为空");
        }

        Map<String, List<Action>> result = new HashMap<>(); // deviceId : List<Action>

        if (runMode == TestTask.RUN_MODE_COMPATIBLE) { // 兼容模式： 所有设备都运行同一份用例
            result = deviceIds.stream().collect(Collectors.toMap(deviceId -> deviceId, v -> testcases));
        } else if (runMode == TestTask.RUN_MODE_EFFICIENCY) { // 高效模式：平均分配用例给设备
            int deviceIndex = 0; //当前分配到第几个设备
            for (int i = 0; i < testcases.size(); i++) {
                List<Action> actions = result.get(deviceIds.get(deviceIndex));
                if (actions == null) {
                    actions = new ArrayList<>();
                    result.put(deviceIds.get(deviceIndex), actions);
                }
                actions.add(testcases.get(i));
                deviceIndex++;
                // 分配完最后一个设备，再从第一个设备开始分配
                if (deviceIndex == deviceIds.size()) {
                    deviceIndex = 0;
                }
            }
        }

        return result;
    }

    /**
     * 保存测试任务
     *
     * @param commitTestTaskRequest
     * @return
     */
    private TestTask saveTestTask(CommitTestTaskRequest commitTestTaskRequest) {
        TestTask testTask = new TestTask();
        BeanUtils.copyProperties(commitTestTaskRequest, testTask);

        testTask.setCommitTime(new Date());
        testTask.setCreatorUid(getUid());
        testTask.setStatus(TestTask.UNFINISHED_STATUS);

        int insertRow;
        try {
            insertRow = testTaskMapper.insertSelective(testTask);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("命名冲突");
        }

        if (insertRow == 1) {
            return testTask;
        } else {
            throw new BusinessException("任务提交失败，请稍后重试");
        }
    }

    /**
     * 查询任务列表
     *
     * @param testTask
     * @param pageRequest
     * @return
     */
    public Response list(TestTask testTask, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<TestTask> testTasks = selectByTestTask(testTask);
        List<TestTaskVo> testTaskVos = testTasks.stream().map(t -> TestTaskVo.convert(t, UserCache.getNickNameById(t.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用testTasks计算total
            long total = Page.getTotal(testTasks);
            return Response.success(Page.build(testTaskVos, total));
        } else {
            return Response.success(testTaskVos);
        }
    }

    public List<TestTask> selectByTestTask(TestTask testTask) {
        if (testTask == null) {
            testTask = new TestTask();
        }

        TestTaskExample testTaskExample = new TestTaskExample();
        TestTaskExample.Criteria criteria = testTaskExample.createCriteria();

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
        testTaskExample.setOrderByClause("commit_time desc");

        return testTaskMapper.selectByExample(testTaskExample);
    }

    public List<TestTask> findUnFinishedTestTask() {
        TestTask testTask = new TestTask();
        testTask.setStatus(TestTask.UNFINISHED_STATUS);
        return selectByTestTask(testTask);
    }

    public int updateByPrimaryKeySelective(TestTask testTask) {
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

        Integer projectId = testTask.getProjectId();
        Project query = new Project();
        query.setId(projectId);
        Project project = projectService.selectByProject(query).get(0);

        TestTaskSummary summary = new TestTaskSummary();
        BeanUtils.copyProperties(testTask, summary);
        summary.setPlatform(project.getPlatform());
        summary.setProjectName(project.getName());
        summary.setCommitorNickName(UserCache.getNickNameById(testTask.getCreatorUid()));

        Integer passCaseCount = testTask.getPassCaseCount();
        Integer totalCaseCount = testTask.getPassCaseCount() + testTask.getFailCaseCount() + testTask.getSkipCaseCount();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2); // 精确到小数点2位

        summary.setPassPercent(numberFormat.format(passCaseCount.floatValue() * 100 / totalCaseCount) + "%");

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

        List<DeviceTestTask> deviceTestTasks = deviceTestTaskService.findByTestTaskId(testTaskId);

        if (!CollectionUtils.isEmpty(deviceTestTasks)) {
            List<DeviceTestTask> alreadyStartedDeviceTestTasks = deviceTestTasks.stream().filter(deviceTestTask -> deviceTestTask.getStatus() != DeviceTestTask.UNSTART_STATUS).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(alreadyStartedDeviceTestTasks)) {
                // 有设备已经运行过测试任务，不让删除整个testTask
                String alreadyStartedDeviceIds = alreadyStartedDeviceTestTasks.stream().map(DeviceTestTask::getDeviceId).collect(Collectors.joining("、"));
                return Response.fail(alreadyStartedDeviceIds + "运行过测试任务，无法删除");
            } else {
                // 批量删除deviceTestTask
                int deleteRow = deviceTestTaskService.deleteInBatch(deviceTestTasks.stream().map(DeviceTestTask::getId).collect(Collectors.toList()));
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
