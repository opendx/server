package com.daxiang.controller;

import com.daxiang.mbg.po.App;
import com.daxiang.model.Response;
import com.daxiang.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/upload")
    public Response upload(@Valid App app, MultipartFile file, HttpServletRequest request) {
        return appService.upload(app, file, request);
    }
}
