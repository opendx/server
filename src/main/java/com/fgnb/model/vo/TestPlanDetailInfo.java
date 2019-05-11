package com.fgnb.model.vo;

import com.fgnb.mbg.po.Action;
import com.fgnb.mbg.po.TestPlan;
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
