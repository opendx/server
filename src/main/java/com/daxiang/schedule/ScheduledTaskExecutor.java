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

        // todo 批量更新
        deviceTestTaskService.findByTestTaskIds(testTaskIds).stream()
                .collect(Collectors.groupingBy(DeviceTestTask::getTestTaskId)) // 按照testTaskId分组
                .forEach((testTaskId, deviceTestTasks) -> {
                    // 所有设备都测试完成
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

                        int passCount = result.getOrDefault(Testcase.PASS_STATUS, 0L).intValue();
                        testTask.setPassCaseCount(passCount);

                        int failCount = result.getOrDefault(Testcase.FAIL_STATUS, 0L).intValue();
                        testTask.setFailCaseCount(failCount);

                        int skipCount = result.getOrDefault(Testcase.SKIP_STATUS, 0L).intValue();
                        testTask.setSkipCaseCount(skipCount);

                        testTaskService.updateByPrimaryKeySelective(testTask);
                        log.info("测试结果统计完成, testTaskId: {}", testTaskId);
                    }
                });
    }
}
