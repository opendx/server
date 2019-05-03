package com.yqhp.controller;

import com.yqhp.mbg.po.TestSuite;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.service.TestSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 * <columnOverride column="testcases" typeHandler="com.yqhp.typehandler.list.IntegerTypeHandler" javaType="java.util.List&lt;Integer&gt;" jdbcType="LONGVARCHAR"/>
 */
@RestController
@RequestMapping("/testSuite")
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService;

    /**
     * 添加测试集
     *
     * @param testSuite
     * @return
     */
    @PostMapping("/add")
    public Response add(@Valid @RequestBody TestSuite testSuite) {
        return testSuiteService.add(testSuite);
    }

    /**
     * 删除测试集
     *
     * @param testSuiteId
     * @return
     */
    @GetMapping("/delete/{testSuiteId}")
    public Response delete(@PathVariable Integer testSuiteId) {
        return testSuiteService.delete(testSuiteId);
    }

    /**
     * 修改testSuite
     *
     * @param testSuite
     * @return
     */
    @PostMapping("/update")
    public Response update(@Valid @RequestBody TestSuite testSuite) {
        return testSuiteService.update(testSuite);
    }


    /**
     * 查询测试集列表
     *
     * @return
     */
    @PostMapping("/list")
    public Response list(TestSuite testSuite, PageRequest pageRequest) {
        return testSuiteService.list(testSuite, pageRequest);
    }


}
