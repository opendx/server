package com.daxiang.controller;

import com.daxiang.mbg.po.Driver;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.DriverVo;
import com.daxiang.service.DriverService;
import com.daxiang.validator.group.UpdateGroup;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        driverService.add(driver);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{driverId}")
    public Response delete(@PathVariable Integer driverId) {
        driverService.deleteAndClearRelatedRes(driverId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Driver driver) {
        driverService.update(driver);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Driver query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<DriverVo> pagedData = driverService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<DriverVo> driverVos = driverService.getDriverVos(query, orderBy);
            return Response.success(driverVos);
        }
    }

    @PostMapping("/downloadUrl")
    public Response getDownloadUrl(Integer type, String deviceId, Integer platform) {
        String url = driverService.getDownloadUrl(type, deviceId, platform);
        return Response.success(ImmutableMap.of("downloadUrl", url));
    }

}
