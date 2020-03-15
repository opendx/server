package com.daxiang.dao;

import com.daxiang.mbg.po.TestSuite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestSuiteDao {
    List<TestSuite> selectByActionId(@Param("actionId") Integer actionId);
}
