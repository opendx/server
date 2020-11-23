package com.daxiang.controller;

import com.daxiang.model.PageRequest;
import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.dto.Testcase;
import com.daxiang.service.DeviceTestTaskService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/deviceTestTask")
public class DeviceTestTaskController {

    @Autowired
    private DeviceTestTaskService deviceTestTaskService;

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({UpdateGroup.class}) DeviceTestTask deviceTestTask) {
        deviceTestTaskService.update(deviceTestTask);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(DeviceTestTask query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<DeviceTestTask> pagedData = deviceTestTaskService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<DeviceTestTask> deviceTestTasks = deviceTestTaskService.getDeviceTestTasks(query, orderBy);
            return Response.success(deviceTestTasks);
        }
    }

    @PostMapping("/{deviceTestTaskId}/updateTestcase")
    public Response updateTestcase(@PathVariable Integer deviceTestTaskId, @RequestBody Testcase testcase) {
        deviceTestTaskService.updateTestcase(deviceTestTaskId, testcase);
        return Response.success("更新成功");
    }

    @GetMapping("/firstUnStart/device/{deviceId}")
    public Response getFirstUnStartDeviceTestTask(@PathVariable String deviceId) {
        DeviceTestTask deviceTestTask = deviceTestTaskService.getFirstUnStartDeviceTestTask(deviceId);
        return Response.success(deviceTestTask);
    }

    @DeleteMapping("/{deviceTestTaskId}")
    public Response delete(@PathVariable Integer deviceTestTaskId) {
        deviceTestTaskService.deleteAndClearRelatedRes(deviceTestTaskId);
        return Response.success("删除成功");
    }

}
