package com.fgnb.controller;

import com.fgnb.mbg.po.DeviceTestTask;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.Testcase;
import com.fgnb.service.DeviceTestTaskService;
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
    @GetMapping("/unStart")
    public Response findUnStartDeviceTestTasksByDeviceIds(String[] deviceIds) {
        return deviceTestTaskService.findUnStartDeviceTestTasksByDeviceIds(deviceIds);
    }
}
