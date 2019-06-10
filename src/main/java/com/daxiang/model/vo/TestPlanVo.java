package com.daxiang.model.vo;

import com.daxiang.mbg.po.TestPlan;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class TestPlanVo extends TestPlan {
    private String creatorNickName;

    public static TestPlanVo convert(TestPlan testPlan, String creatorNickName) {
        TestPlanVo testPlanVo = new TestPlanVo();
        BeanUtils.copyProperties(testPlan, testPlanVo);
        testPlanVo.setCreatorNickName(creatorNickName);
        return testPlanVo;
    }
}
