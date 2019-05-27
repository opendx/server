package com.fgnb.schedule;

import com.fgnb.mbg.po.DeviceTestTask;
import com.fgnb.mbg.po.TestTask;
import com.fgnb.model.vo.Testcase;
import com.fgnb.service.DeviceTestTaskService;
import com.fgnb.service.TestTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Component
public class ScheduledTaskExcutor {
    @Autowired
    private TestTaskService testTaskService;
    @Autowired
    private DeviceTestTaskService deviceTestTaskService;

    /**
     * 统计已完成的测试任务
     */
    @Scheduled(fixedRate = 60000)
    public void statisticsFinishedTestTask() {
        // 未完成的测试任务
        List<TestTask> unFinishedTestTasks = testTaskService.findUnFinishedTestTask();
        if(CollectionUtils.isEmpty(unFinishedTestTasks)) {
            return;
        }

        unFinishedTestTasks.stream().parallel().forEach(unFinishedTestTask -> {
            List<DeviceTestTask> deviceTestTasks = deviceTestTaskService.findByTestTaskId(unFinishedTestTask.getId());
            if(!CollectionUtils.isEmpty(deviceTestTasks)) {
                long finishedCount = deviceTestTasks.stream().filter(task -> task.getStatus() == DeviceTestTask.FINISHED_STATUS).count();
                // 所有手机都测试完成
                if(deviceTestTasks.size() == finishedCount) {
                    log.info("开始统计测试任务结果: {}", unFinishedTestTask.getId());
                    List<Testcase> testcases = deviceTestTasks.stream().flatMap(task -> task.getTestcases().stream()).collect(Collectors.toList());

                    long passCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.PASS_STATUS).count();
                    long failCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.FAIL_STATUS).count();
                    long skipCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.SKIP_STATUS).count();

                    TestTask testTask = new TestTask();
                    testTask.setId(unFinishedTestTask.getId());
                    testTask.setStatus(TestTask.FINISHED_STATUS);
                    testTask.setFinishTime(new Date());
                    testTask.setPassCaseCount((int) passCount);
                    testTask.setFailCaseCount((int) failCount);
                    testTask.setSkipCaseCount((int) skipCount);

                    testTaskService.updateByPrimaryKeySelective(testTask);
                    log.info("测试任务结果统计完成: {}", unFinishedTestTask.getId());
                }
            }
        });
    }
}
