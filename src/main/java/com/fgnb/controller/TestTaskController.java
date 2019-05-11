package com.fgnb.controller;

import com.fgnb.mbg.po.TestTask;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.request.CommitTestTaskRequest;
import com.fgnb.service.TestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
     * @param commitTestTaskRequest
     * @return
     */
    @PostMapping("/commit")
    public Response commit(@RequestBody @Valid CommitTestTaskRequest commitTestTaskRequest) {
        return testTaskService.commit(commitTestTaskRequest);
    }

    @PostMapping("/list")
    public Response list(TestTask testTask, PageRequest pageRequest) {
        return testTaskService.list(testTask,pageRequest);
    }
}
