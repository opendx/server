package com.daxiang.controller;

import com.daxiang.mbg.po.TestTask;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.TestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/testTask")
public class TestTaskController {

    @Autowired
    private TestTaskService testTaskService;

    /**
     * 提交测试任务
     *
     * @return
     */
    @GetMapping("/commit")
    public Response commit(Integer testPlanId) {
        return testTaskService.commit(testPlanId, null);
    }

    /**
     * 测试任务列表
     *
     * @param testTask
     * @param pageRequest
     * @return
     */
    @PostMapping("/list")
    public Response list(TestTask testTask, PageRequest pageRequest) {
        return testTaskService.list(testTask, pageRequest);
    }

    /**
     * 测试任务概要
     *
     * @param testTaskId
     * @return
     */
    @GetMapping("/{testTaskId}/summary")
    public Response getTestTaskSummary(@PathVariable Integer testTaskId) {
        return testTaskService.getTestTaskSummary(testTaskId);
    }

    @DeleteMapping("/{testTaskId}")
    public Response delete(@PathVariable Integer testTaskId) {
        return testTaskService.delete(testTaskId);
    }
}
