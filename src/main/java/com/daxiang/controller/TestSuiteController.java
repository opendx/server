package com.daxiang.controller;

import com.daxiang.mbg.po.TestSuite;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.TestSuiteService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/add")
    public Response add(@Valid @RequestBody TestSuite testSuite) {
        return testSuiteService.add(testSuite);
    }

    @DeleteMapping("/{testSuiteId}")
    public Response delete(@PathVariable Integer testSuiteId) {
        return testSuiteService.delete(testSuiteId);
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({UpdateGroup.class}) TestSuite testSuite) {
        return testSuiteService.update(testSuite);
    }

    @PostMapping("/list")
    public Response list(TestSuite testSuite, PageRequest pageRequest) {
        return testSuiteService.list(testSuite, pageRequest);
    }

}
