package com.fgnb.dao;

import com.fgnb.mbg.po.TestPlan;
import com.fgnb.model.vo.TestPlanVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestPlanDao {
    List<TestPlanVo> selectByTestPlan(TestPlan testPlan);
}
