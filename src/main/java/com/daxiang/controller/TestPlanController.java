package com.daxiang.controller;

import com.daxiang.mbg.po.TestPlan;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.TestPlanVo;
import com.daxiang.service.TestPlanService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/testPlan")
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    @PostMapping("/add")
    public Response addTestPlan(@RequestBody @Valid TestPlan testPlan) {
        testPlanService.add(testPlan);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{testPlanId}")
    public Response deleteTestPlan(@PathVariable Integer testPlanId) {
        testPlanService.delete(testPlanId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response updateTestPlan(@RequestBody @Validated({UpdateGroup.class}) TestPlan testPlan) {
        testPlanService.update(testPlan);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(TestPlan query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<TestPlanVo> pagedData = testPlanService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<TestPlanVo> testPlanVos = testPlanService.getTestPlanVos(query, orderBy);
            return Response.success(testPlanVos);
        }
    }
}
