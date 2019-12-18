package com.daxiang.controller;

import com.daxiang.mbg.po.App;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.AppService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/upload")
    public Response upload(@Valid App app, MultipartFile file) {
        return appService.upload(app, file);
    }

    @DeleteMapping("/{appId}")
    public Response delete(@PathVariable Integer appId) {
        return appService.delete(appId);
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody App app) {
        return appService.update(app);
    }

    @PostMapping("/list")
    public Response list(App app, PageRequest pageRequest) {
        return appService.list(app, pageRequest);
    }

    @GetMapping("/{appId}/aaptDumpBadging")
    public Response aaptDumpBadging(@PathVariable Integer appId) {
        return appService.aaptDumpBadging(appId);
    }
}
