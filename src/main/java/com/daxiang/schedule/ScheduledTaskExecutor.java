package com.daxiang.schedule;

import com.daxiang.model.dto.Testcase;
import com.daxiang.service.FileService;
import com.daxiang.service.TestTaskService;
import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.mbg.po.TestTask;
import com.daxiang.service.DeviceTestTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private FileService fileService;

    /**
     * 统计已完成的测试任务
     */
    @Transactional
    @Scheduled(fixedRate = 10000)
    public void statisticsFinishedTestTask() {
        // 未完成的测试任务
        List<TestTask> testTasks = testTaskService.getUnFinishedTestTasks();
        if (CollectionUtils.isEmpty(testTasks)) {
            return;
        }

        List<Integer> testTaskIds = testTasks.stream().map(TestTask::getId).collect(Collectors.toList());

        // todo 批量更新
        deviceTestTaskService.getDeviceTestTasksByTestTaskIds(testTaskIds).stream()
                .collect(Collectors.groupingBy(DeviceTestTask::getTestTaskId)) // 按照testTaskId分组
                .forEach((testTaskId, deviceTestTasks) -> {
                    // 所有device都测试完成
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
                        Date maxEndTime = testcases.stream().map(Testcase::getEndTime).max(Comparator.naturalOrder()).get();
                        testTask.setFinishTime(maxEndTime);

                        int passCount = result.getOrDefault(Testcase.PASS_STATUS, 0L).intValue();
                        testTask.setPassCaseCount(passCount);

                        int failCount = result.getOrDefault(Testcase.FAIL_STATUS, 0L).intValue();
                        testTask.setFailCaseCount(failCount);

                        int skipCount = result.getOrDefault(Testcase.SKIP_STATUS, 0L).intValue();
                        testTask.setSkipCaseCount(skipCount);

                        testTaskService.update(testTask);
                        log.info("测试结果统计完成, testTaskId: {}", testTaskId);
                    }
                });
    }


    /**
     * 每天0点，清理3天前的临时文件
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearTmpFiles() {
        int beforeDays = 3;
        log.info("开始清除{}天前的临时文件", beforeDays);
        int deletedTmpFilesCount = fileService.clearTmpFilesBefore(beforeDays);
        log.info("清理临时文件完成，共清理{}个文件", deletedTmpFilesCount);
    }
}
