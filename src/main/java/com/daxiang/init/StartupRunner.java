package com.daxiang.init;

import com.daxiang.model.UploadFile;
import com.daxiang.service.TestPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TestPlanService testPlanService;

    @Value("${static-location}/")
    private String staticLocation;

    @Override
    public void run(ApplicationArguments args) {
        String uploadImgPath = staticLocation + UploadFile.IMG_PATH;
        String uploadVideoPath = staticLocation + UploadFile.VIDEO_PATH;
        String uploadAppPath = staticLocation + UploadFile.APP_PATH;
        String uploadDriverPath = staticLocation + UploadFile.DRIVER_PATH;
        String uploadOtherFilePath = staticLocation + UploadFile.OTHER_FILE_PATH;

        File uploadImgDir = new File(uploadImgPath);
        if (!uploadImgDir.exists()) {
            log.info("创建img上传存放目录 -> {}", uploadImgPath);
            uploadImgDir.mkdirs();
        }

        File uploadVideoDir = new File(uploadVideoPath);
        if (!uploadVideoDir.exists()) {
            log.info("创建video上传存放目录 -> {}", uploadVideoPath);
            uploadVideoDir.mkdirs();
        }

        File uploadAppDir = new File(uploadAppPath);
        if (!uploadAppDir.exists()) {
            log.info("创建app上传存放目录 -> {}", uploadAppPath);
            uploadAppDir.mkdirs();
        }

        File uploadDriverDir = new File(uploadDriverPath);
        if (!uploadDriverDir.exists()) {
            log.info("创建driver上传存放目录 -> {}", uploadDriverPath);
            uploadDriverDir.mkdirs();
        }

        File uploadOtherFileDir = new File(uploadOtherFilePath);
        if (!uploadOtherFileDir.exists()) {
            log.info("创建other file上传存放目录 -> {}", uploadOtherFilePath);
            uploadOtherFileDir.mkdirs();
        }

        // 启动server时，按cron表达式执行所有开启的定时任务
        testPlanService.scheduleEnabledTasks();
    }
}
