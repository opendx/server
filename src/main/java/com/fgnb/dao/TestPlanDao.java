package com.fgnb.dao;

import com.fgnb.mbg.po.TestPlan;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestPlanDao {
    List<TestPlan> selectByTestSuiteId(Integer testSuiteId);
}
