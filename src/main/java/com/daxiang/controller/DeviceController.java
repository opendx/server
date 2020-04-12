package com.daxiang.controller;

import com.daxiang.mbg.po.Device;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/save")
    public Response save(@RequestBody @Valid Device device) {
        return deviceService.save(device);
    }

    @PostMapping("/list")
    public Response list(Device device, PageRequest pageRequest) {
        return deviceService.list(device, pageRequest);
    }

    @GetMapping("/{deviceId}/start")
    public Response start(@PathVariable String deviceId) {
        return deviceService.start(deviceId);
    }

    @GetMapping("/online/platform/{platform}")
    public Response getOnlineDevices(@PathVariable Integer platform) {
        return deviceService.getOnlineDevices(platform);
    }

}
