package com.daxiang.init;

import com.daxiang.service.FileService;
import com.daxiang.service.TestPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jiangyitao.
 */
@Component
@Slf4j
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private FileService fileService;

    @Override
    public void run(ApplicationArguments args) {
        fileService.mkUploadDirIfNotExists();
        // 启动server时，按cron表达式执行所有开启的定时任务
        testPlanService.scheduleEnabledTasks();
    }
}
