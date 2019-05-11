package com.fgnb.model.vo;

import com.fgnb.mbg.po.TestPlan;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class TestPlanVo extends TestPlan{
    private String creatorNickName;
}
