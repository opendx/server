package com.daxiang.init;

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
    @Value("${web.upload-apk-path}")
    private String uploadApkPath;
    @Value("${web.upload-ipa-path}")
    private String uploadIpaPath;
    @Value("${web.upload-other-path}")
    private String uploadOtherPath;
    @Autowired
    private UserService userService;

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

        File uploadApkDir = new File(uploadApkPath);
        if (!uploadApkDir.exists()) {
            log.info("创建apk上传存放目录 -> {}", uploadApkPath);
            uploadApkDir.mkdirs();
        }

        File uploadIpaDir = new File(uploadIpaPath);
        if (!uploadIpaDir.exists()) {
            log.info("创建ipa上传存放目录 -> {}", uploadIpaPath);
            uploadIpaDir.mkdirs();
        }

        File uploadOtherDir = new File(uploadOtherPath);
        if (!uploadOtherDir.exists()) {
            log.info("创建other上传存放目录 -> {}", uploadOtherPath);
            uploadOtherDir.mkdirs();
        }

        // 首次启动将所有用户放入单机缓存，以后有集群需求再用redis
        UserCache.set(userService.selectAll().stream().collect(Collectors.toMap(User::getId, user -> user)));
    }
}
