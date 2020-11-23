package com.daxiang.controller;

import com.daxiang.mbg.po.TestTask;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.TestTaskSummary;
import com.daxiang.model.vo.TestTaskVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.service.TestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/testTask")
public class TestTaskController {

    @Autowired
    private TestTaskService testTaskService;

    @GetMapping("/commit")
    public Response commit(Integer testPlanId) {
        testTaskService.commit(testPlanId, SecurityUtil.getCurrentUserId());
        return Response.success("提交成功");
    }

    @PostMapping("/list")
    public Response list(TestTask query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<TestTaskVo> pagedData = testTaskService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<TestTaskVo> testTaskVos = testTaskService.getTestTaskVos(query, orderBy);
            return Response.success(testTaskVos);
        }
    }

    @GetMapping("/{testTaskId}/summary")
    public Response getTestTaskSummary(@PathVariable Integer testTaskId) {
        TestTaskSummary testTaskSummary = testTaskService.getTestTaskSummary(testTaskId);
        return Response.success(testTaskSummary);
    }

    @DeleteMapping("/{testTaskId}")
    public Response delete(@PathVariable Integer testTaskId) {
        testTaskService.deleteAndClearRelatedRes(testTaskId);
        return Response.success("删除成功");
    }
}
