package com.daxiang.init;

import com.daxiang.service.TestPlanService;
import com.daxiang.service.UserService;
import com.daxiang.mbg.po.User;
import com.daxiang.model.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Component
@Slf4j
public class StartupRunner implements ApplicationRunner {
    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;
    @Value("${web.upload-app-path}")
    private String uploadAppPath;
    @Value("${web.upload-driver-path}")
    private String uploadDriverPath;
    @Value("${web.upload-other-path}")
    private String uploadOtherPath;

    @Autowired
    private UserService userService;
    @Autowired
    private TestPlanService testPlanService;

    @Override
    public void run(ApplicationArguments args) {
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

        File uploadOtherDir = new File(uploadOtherPath);
        if (!uploadOtherDir.exists()) {
            log.info("创建other上传存放目录 -> {}", uploadOtherPath);
            uploadOtherDir.mkdirs();
        }

        // 启动server时，将所有用户放入单机缓存，以后有集群需求再用redis
        UserCache.set(userService.selectAll().stream().collect(Collectors.toMap(User::getId, user -> user)));
        // 启动server时，按cron表达式执行所有开启的定时任务
        testPlanService.scheduleEnabledTasks();
    }
}
