package com.daxiang.schedule;

import com.daxiang.model.vo.Testcase;
import com.daxiang.service.TestTaskService;
import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.mbg.po.TestTask;
import com.daxiang.service.DeviceTestTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Component
public class ScheduledTaskExecutor {

    @Autowired
    private TestTaskService testTaskService;
    @Autowired
    private DeviceTestTaskService deviceTestTaskService;

    /**
     * 统计已完成的测试任务
     */
    @Scheduled(fixedRate = 15000)
    public void statisticsFinishedTestTask() {
        // 未完成的测试任务
        List<TestTask> unFinishedTestTasks = testTaskService.findUnFinishedTestTask();
        if (CollectionUtils.isEmpty(unFinishedTestTasks)) {
            return;
        }

        List<Integer> testTaskIds = unFinishedTestTasks.stream().map(TestTask::getId).collect(Collectors.toList());

        deviceTestTaskService.findByTestTaskIds(testTaskIds).stream()
                .collect(Collectors.groupingBy(DeviceTestTask::getTestTaskId)) // 按照testTaskId分组
                .forEach((testTaskId, deviceTestTasks) -> {
                    // 所有手机都测试完成
                    boolean allDeviceTestFinished = deviceTestTasks.stream()
                            .allMatch(task -> task.getStatus() == DeviceTestTask.FINISHED_STATUS);

                    if (allDeviceTestFinished) {
                        log.info("开始统计测试结果, testTaskId: {}", testTaskId);
                        List<Testcase> testcases = deviceTestTasks.stream()
                                .flatMap(task -> task.getTestcases().stream()).collect(Collectors.toList());

                        // 按测试用例结果分组
                        Map<Integer, Long> result = testcases.stream()
                                .collect(Collectors.groupingBy(Testcase::getStatus, Collectors.counting()));

                        TestTask testTask = new TestTask();
                        testTask.setId(testTaskId);
                        testTask.setStatus(TestTask.FINISHED_STATUS);

                        // 所有用例结束时间最晚的作为完成时间
                        Date finishTime = testcases.stream()
                                .map(Testcase::getEndTime)
                                .sorted(Comparator.reverseOrder())
                                .findFirst().get();
                        testTask.setFinishTime(finishTime);

                        Long passCount = result.get(Testcase.PASS_STATUS);
                        if (passCount != null) {
                            testTask.setPassCaseCount(Math.toIntExact(passCount));
                        }

                        Long failCount = result.get(Testcase.FAIL_STATUS);
                        if (failCount != null) {
                            testTask.setFailCaseCount(Math.toIntExact(failCount));
                        }

                        Long skipCount = result.get(Testcase.SKIP_STATUS);
                        if (skipCount != null) {
                            testTask.setSkipCaseCount(Math.toIntExact(skipCount));
                        }

                        testTaskService.updateByPrimaryKeySelective(testTask);
                        log.info("测试结果统计完成, testTaskId: {}", testTaskId);
                    }
                });
    }
}
