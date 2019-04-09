package com.yqhp.dao;

import com.yqhp.mbg.po.TestPlan;
import com.yqhp.model.vo.TestPlanVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestPlanDao {
    List<TestPlanVo> selectByTestPlan(TestPlan testPlan);
}
