package com.yqhp.model.vo;

import com.yqhp.mbg.po.TestTask;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class TestTaskVo extends TestTask {
    private String creatorNickName;
    private String testPlanName;
}
