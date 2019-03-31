package com.yqhp.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by jiangyitao.
 */
@Component
@Slf4j
public class StartupRunner implements ApplicationRunner {
    //上传图片存放路径
    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    //上传视频存放路径
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;
    //数据库
    @Value("${db-url}")
    private String dbURL;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("dbURL -> {}", dbURL);
        File uploadImgDir = new File(uploadImgPath);
        if (!uploadImgDir.exists()) {
            log.info("创建图片上传存放目录 -> {}", uploadImgPath);
            uploadImgDir.mkdirs();
        }
        File uploadVideoDir = new File(uploadVideoPath);
        if (!uploadVideoDir.exists()) {
            log.info("创建视频上传存放目录 -> {}", uploadVideoPath);
            uploadVideoDir.mkdirs();
        }
    }
}
