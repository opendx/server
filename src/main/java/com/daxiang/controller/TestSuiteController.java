package com.daxiang.controller;

import com.daxiang.mbg.po.TestSuite;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.TestSuiteVo;
import com.daxiang.service.TestSuiteService;
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
@RequestMapping("/testSuite")
public class TestSuiteController {

    @Autowired
    private TestSuiteService testSuiteService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody TestSuite testSuite) {
        testSuiteService.add(testSuite);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{testSuiteId}")
    public Response delete(@PathVariable Integer testSuiteId) {
        testSuiteService.delete(testSuiteId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({UpdateGroup.class}) TestSuite testSuite) {
        testSuiteService.update(testSuite);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(TestSuite query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<TestSuiteVo> pagedData = testSuiteService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<TestSuiteVo> testSuiteVos = testSuiteService.getTestSuiteVos(query, orderBy);
            return Response.success(testSuiteVos);
        }
    }

}
