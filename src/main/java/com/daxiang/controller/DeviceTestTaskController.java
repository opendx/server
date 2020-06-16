package com.daxiang.controller;

import com.daxiang.model.PageRequest;
import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.model.Response;
import com.daxiang.model.dto.Testcase;
import com.daxiang.service.DeviceTestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiangyitao.
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
    public Response list(DeviceTestTask testTaskDevice, PageRequest pageRequest) {
        return deviceTestTaskService.list(testTaskDevice, pageRequest);
    }

    /**
     * 更新device的测试用例运行信息
     *
     * @param deviceTestTaskId
     * @param testcase
     * @return
     */
    @PostMapping("/{deviceTestTaskId}/updateTestcase")
    public Response updateTestcase(@PathVariable Integer deviceTestTaskId, @RequestBody Testcase testcase) {
        return deviceTestTaskService.updateTestcase(deviceTestTaskId, testcase);
    }

    /**
     * 通过deviceId查询未开始的测试任务（最开始的一条）
     *
     * @return
     */
    @GetMapping("/firstUnStart/device/{deviceId}")
    public Response getFirstUnStartDeviceTestTask(@PathVariable String deviceId) {
        return deviceTestTaskService.getFirstUnStartDeviceTestTask(deviceId);
    }

    @DeleteMapping("/{deviceTestTaskId}")
    public Response delete(@PathVariable Integer deviceTestTaskId) {
        return deviceTestTaskService.delete(deviceTestTaskId);
    }

}
