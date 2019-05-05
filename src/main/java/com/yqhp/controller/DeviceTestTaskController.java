package com.yqhp.controller;

import com.yqhp.mbg.po.DeviceTestTask;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.vo.Testcase;
import com.yqhp.service.DeviceTestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 * <columnOverride column="global_vars" typeHandler="com.yqhp.typehandler.GlobalVarListTypeHandler" javaType="java.util.List&lt;com.yqhp.mbg.po.GlobalVar&gt;" jdbcType="LONGVARCHAR"/>
 * <columnOverride column="before_suite" typeHandler="com.yqhp.typehandler.ActionTypeHandler" javaType="com.yqhp.mbg.po.Action" jdbcType="LONGVARCHAR"/>
 * <columnOverride column="testcases" typeHandler="com.yqhp.typehandler.TestcaseListTypeHandler" javaType="java.util.List&lt;com.yqhp.model.vo.Testcase&gt;" jdbcType="LONGVARCHAR"/>
 */
@RestController
@RequestMapping("/deviceTestTask")
public class DeviceTestTaskController {

    @Autowired
    private DeviceTestTaskService deviceTestTaskService;

    @PostMapping("/update")
    public Response update(@RequestBody DeviceTestTask deviceTestTask) {
        return deviceTestTaskService.update(deviceTestTask);
    }

    @PostMapping("/list")
    public Response list(DeviceTestTask testTaskDevice,PageRequest pageRequest) {
        return deviceTestTaskService.list(testTaskDevice,pageRequest);
    }

    /**
     * 更新设备的测试用例运行信息
     * @param deviceTestTaskId
     * @param testcase
     * @return
     */
    @PostMapping("/updateTestcase/{deviceTestTaskId}")
    public Response updateTestcase(@PathVariable Integer deviceTestTaskId, @RequestBody Testcase testcase) {
        return deviceTestTaskService.updateTestcase(deviceTestTaskId,testcase);
    }

    /**
     * 通过设备id查询当前未开始的测试任务
     * @param deviceIds
     * @return
     */
    @GetMapping("/findUnStartTestTasksByDeviceIds")
    public Response findUnStartTestTasksByDeviceIds(String[] deviceIds) {
        return deviceTestTaskService.findUnStartTestTasksByDeviceIds(deviceIds);
    }
}
