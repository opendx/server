package com.yqhp.controller;

import com.yqhp.mbg.po.TestTaskDevice;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.service.TestTaskDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 * <columnOverride column="before_suite" typeHandler="com.yqhp.typehandler.ActionTypeHandler" javaType="com.yqhp.mbg.po.Action" jdbcType="LONGVARCHAR"/>
 * <columnOverride column="testcases" typeHandler="com.yqhp.typehandler.TestcaseListTypeHandler" javaType="java.util.List&lt;com.yqhp.model.vo.Testcase&gt;" jdbcType="LONGVARCHAR"/>
 */
@RestController
@RequestMapping("/testTaskDevice")
public class TestTaskDeviceController {

    @Autowired
    private TestTaskDeviceService testTaskDeviceService;

    @PostMapping("/update")
    public Response update(@Valid @RequestBody TestTaskDevice testTaskDevice) {
        return testTaskDeviceService.update(testTaskDevice);
    }

    @PostMapping("/list")
    public Response list(TestTaskDevice testTaskDevice,PageRequest pageRequest) {
        return testTaskDeviceService.list(testTaskDevice,pageRequest);
    }

    @GetMapping("/findByDeviceIds")
    public Response findByDeviceIds(String[] deviceIds) {
        return testTaskDeviceService.findByDeviceIds(deviceIds);
    }
}
