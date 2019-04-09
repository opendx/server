package com.yqhp.controller;

import com.yqhp.mbg.po.TestPlan;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 * <columnOverride column="test_suites" typeHandler="com.yqhp.typehandler.IntegerTypeHandler" javaType="java.util.List&lt;Integer&gt;" jdbcType="LONGVARCHAR"/>
 * <columnOverride column="befores" typeHandler="com.yqhp.typehandler.BeforeTypeHandler" javaType="java.util.List&lt;com.yqhp.model.testplan.Before&gt;" jdbcType="LONGVARCHAR"/>
 */
@RestController
@RequestMapping("/testPlan")
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    /**
     * 添加测试计划
     *
     * @param testPlan
     * @return
     */
    @PostMapping("/add")
    public Response addTestPlan(@RequestBody @Valid TestPlan testPlan) {
        return testPlanService.add(testPlan);
    }

    /**
     * 删除测试计划
     *
     * @param testPlanId
     * @return
     */
    @GetMapping("/delete/{testPlanId}")
    public Response deleteTestPlan(@PathVariable Integer testPlanId) {
        return testPlanService.delete(testPlanId);
    }

    /**
     * 更新测试计划
     *
     * @param testPlan
     * @return
     */
    @PostMapping("/update")
    public Response updateTestPlan(@RequestBody @Valid TestPlan testPlan) {
        return testPlanService.update(testPlan);
    }

    /**
     * 查询测试计划列表
     *
     * @param testPlan
     * @param pageRequest
     * @return
     */
    @PostMapping("/list")
    public Response list(TestPlan testPlan, PageRequest pageRequest) {
        return testPlanService.list(testPlan, pageRequest);
    }

}
