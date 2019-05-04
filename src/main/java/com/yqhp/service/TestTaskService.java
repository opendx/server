package com.yqhp.service;

import com.yqhp.exception.BusinessException;
import com.yqhp.mbg.mapper.*;
import com.yqhp.mbg.po.*;
import com.yqhp.model.Response;
import com.yqhp.model.action.Step;
import com.yqhp.model.request.CommitTestTaskRequest;
import com.yqhp.model.testplan.Before;
import com.yqhp.model.vo.Testcase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private TestPlanMapper testPlanMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private DeviceTestTaskMapper deviceTestTaskMapper;

    /**
     * 提交测试任务
     *
     * @param commitTestTaskRequest
     * @return
     */
    @Transactional
    public Response commit(CommitTestTaskRequest commitTestTaskRequest) {
        TestTask testTask = saveTestTask(commitTestTaskRequest);

        TestPlan testPlan = testPlanMapper.selectByPrimaryKey(testTask.getTestPlanId());
        List<Action> testcases = testPlanService.getTestcasesByTestPlan(testPlan);

        //在每个用例的步骤插入beforeMethodAction，作为第0步
        Action beforeMethodAction = getActionByBeforeType(testPlan.getBefores(), Before.BEFORE_METHOD_TYPE);
        if (beforeMethodAction != null) {
            testcases.forEach(testcase -> {
                Step step = new Step();
                step.setNumber(0);
                step.setActionId(beforeMethodAction.getId());
                step.setName("BeforeMethod - " + beforeMethodAction.getName());
                testcase.getSteps().add(0, step);
            });
        }

        List<Action> needBuildActions = new ArrayList<>(testcases);
        Action beforeSuiteAction = getActionByBeforeType(testPlan.getBefores(), Before.BEFORE_SUITE_TYPE);
        if (beforeSuiteAction != null) {
            needBuildActions.add(beforeSuiteAction);
        }

        new ActionTreeBuilder(actionMapper).build(needBuildActions);

        //根据不同用例分发策略，给设备分配用例
        Map<String, List<Action>> deviceTestcases = allocateTestcaseToDevice(commitTestTaskRequest.getDeviceIds(), testcases, testTask.getRunMode());

        deviceTestcases.forEach((deviceId, actions) -> {
            DeviceTestTask deviceTestTask = new DeviceTestTask();
            deviceTestTask.setTestTaskId(testTask.getId());
            deviceTestTask.setDeviceId(deviceId);
            if(beforeSuiteAction != null) {
                deviceTestTask.setBeforeSuite(beforeSuiteAction);
            }
            List<Testcase> cases = actions.stream().map(action -> {
                Testcase testcase = new Testcase();
                BeanUtils.copyProperties(action, testcase);
                return testcase;
            }).collect(Collectors.toList());
            deviceTestTask.setTestcases(cases);
            deviceTestTask.setStatus(DeviceTestTask.UNSTART_STATUS);
            int insertRow = deviceTestTaskMapper.insertSelective(deviceTestTask);
            if(insertRow != 1) {
                throw new BusinessException( deviceId + "保存测试任务失败");
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
        if (CollectionUtils.isEmpty(deviceIds) || CollectionUtils.isEmpty(testcases)) {
            throw new BusinessException("设备或用例为空，无法分配");
        }

        Map<String, List<Action>> result = new HashMap<>(); // deviceId : List<Action>

        if (runMode == TestTask.RUN_MODE_COMPATIBLE) { //兼容模式： 所有设备都运行同一份用例
            result = deviceIds.stream().collect(Collectors.toMap(deviceId -> deviceId, v -> testcases));
        } else if (runMode == TestTask.RUN_MODE_EFFICIENCY) { //高效模式：平均分配用例给设备
            int deviceIndex = 0; //当前分配到第几个设备
            for (int i = 0; i < testcases.size(); i++) {
                List<Action> actions = result.get(deviceIds.get(deviceIndex));
                if (actions == null) {
                    actions = new ArrayList<>();
                    result.put(deviceIds.get(deviceIndex), actions);
                }
                actions.add(testcases.get(i));
                deviceIndex++;
                //分配完最后一个设备，再从第一个设备开始分配
                if (deviceIndex == deviceIds.size()) {
                    deviceIndex = 0;
                }
            }
        }

        return result;
    }

    /**
     * 根据不同的beforeType获取相应的action
     *
     * @param befores
     * @param type
     * @return
     */
    private Action getActionByBeforeType(List<Before> befores, Integer type) {
        if (!CollectionUtils.isEmpty(befores)) {
            Before before = befores.stream().filter(b -> b.getType() == type).findFirst().orElse(null);
            if (before != null) {
                return actionMapper.selectByPrimaryKey(before.getActionId());
            }
        }
        return null;
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

        try {
            int insertRow = testTaskMapper.insertSelective(testTask);
            if (insertRow != 1) {
                throw new BusinessException("任务提交失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new BusinessException("命名冲突");
        }

        return testTask;
    }

}
