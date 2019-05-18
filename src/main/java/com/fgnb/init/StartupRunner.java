package com.fgnb.init;

import com.fgnb.mbg.po.User;
import com.fgnb.model.UserCache;
import com.fgnb.service.UserService;
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
    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
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
        // 首次启动将所有用户放入单机缓存，以后有集群需求再用redis
        UserCache.set(userService.selectAll().stream().collect(Collectors.toMap(User::getId, user -> user)));
    }
}
