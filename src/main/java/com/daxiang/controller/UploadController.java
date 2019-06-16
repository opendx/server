package com.daxiang.controller;

import com.daxiang.model.Response;
import com.daxiang.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/file")
    public Response uploadFile(MultipartFile file, HttpServletRequest request) {
       return uploadService.uploadFile(file, request);
    }
}
