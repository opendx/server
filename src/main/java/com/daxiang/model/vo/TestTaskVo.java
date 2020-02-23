package com.daxiang.model.vo;

import com.daxiang.mbg.po.TestTask;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class TestTaskVo extends TestTask {
    private String creatorNickName = "";
}
