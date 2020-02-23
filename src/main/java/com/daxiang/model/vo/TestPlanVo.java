package com.daxiang.model.vo;

import com.daxiang.mbg.po.TestPlan;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class TestPlanVo extends TestPlan {
    private String creatorNickName = "";
}
