package com.daxiang.controller;

import com.daxiang.mbg.po.Driver;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.DriverService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody Driver driver) {
        return driverService.add(driver);
    }

    @DeleteMapping("/{driverId}")
    public Response delete(@PathVariable Integer driverId) {
        return driverService.delete(driverId);
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Driver driver) {
        return driverService.update(driver);
    }

    @PostMapping("/list")
    public Response list(Driver driver, PageRequest pageRequest) {
        return driverService.list(driver, pageRequest);
    }

    @PostMapping("/downloadUrl")
    public Response getDownloadUrl(Integer type, String deviceId, Integer platform) {
        return driverService.getDownloadUrl(type, deviceId, platform);
    }
}
