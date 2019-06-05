package com.fgnb.controller;

import com.fgnb.mbg.po.Device;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.service.DeviceService;
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

    /**
     * 给Agent调用的接口
     * 保存Device信息
     *
     * @param device
     * @return
     */
    @PostMapping("/save")
    public Response save(@RequestBody @Valid Device device) {
        return deviceService.save(device);
    }

    /**
     * 查询设备列表
     *
     * @param device
     * @param pageRequest
     * @return
     */
    @PostMapping("/list")
    public Response list(Device device, PageRequest pageRequest) {
        return deviceService.list(device, pageRequest);
    }

    /**
     * 开始控制手机
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/{deviceId}/start")
    public Response start(@PathVariable String deviceId) {
        return deviceService.start(deviceId);
    }

    /**
     * 获取在线的设备
     * @param platform
     * @return
     */
    @GetMapping("/online/platform/{platform}")
    public Response getOnlineDevices(@PathVariable Integer platform) {
        return deviceService.getOnlineDevices(platform);
    }

}
