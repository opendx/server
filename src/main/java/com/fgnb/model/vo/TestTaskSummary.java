package com.fgnb.model.vo;

import com.fgnb.mbg.po.TestTask;
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
}
