package com.daxiang.model.vo;

import com.daxiang.mbg.po.TestTask;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class TestTaskSummary extends TestTask {
    private Integer platform;
    private String projectName;
    private String commitorNickName;
    private String passPercent;
    private String environmentName;
}
