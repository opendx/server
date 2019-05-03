package com.yqhp.model.vo;

import com.yqhp.mbg.po.Action;
import com.yqhp.mbg.po.TestPlan;
import lombok.Data;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
public class TestPlanDetailInfo extends TestPlan {
    private String beforeMethodName;
    private String beforeSuiteName;
    private List<Action> testcases;
}
