package com.daxiang.dao;

import com.daxiang.mbg.po.TestPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestPlanDao {
    List<TestPlan> selectByTestSuiteId(@Param("testSuiteId") Integer testSuiteId);
    List<TestPlan> selectByActionId(@Param("actionId") Integer actionId);
}
