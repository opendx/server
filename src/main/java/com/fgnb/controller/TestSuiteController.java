package com.fgnb.controller;

import com.fgnb.mbg.po.TestSuite;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.service.TestSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
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
