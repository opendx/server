package com.yqhp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yqhp.mbg.po.Device;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.service.DeviceService;
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
     * 获取设备类型
     */
    @GetMapping("/types")
    public Response getDeviceTypes() {
        JSONObject android = new JSONObject();
        android.put("type", Device.ANDROID_TYPE);
        android.put("name", "Android");

        JSONObject ios = new JSONObject();
        ios.put("type", Device.IOS_TYPE);
        ios.put("name", "iOS");

        JSONArray deviceTypes = new JSONArray();
        deviceTypes.add(android);
        deviceTypes.add(ios);

        return Response.success(deviceTypes);
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
    @GetMapping("/start/{deviceId}")
    public Response start(@PathVariable String deviceId) {
        return deviceService.start(deviceId);
    }

    /**
     * 获取在线的设备
     * @param type
     * @return
     */
    @GetMapping("/online/{type}")
    public Response getOnlineDevices(@PathVariable Integer type) {
        return deviceService.getOnlineDevices(type);
    }
}
