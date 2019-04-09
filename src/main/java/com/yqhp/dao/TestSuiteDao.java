package com.yqhp.dao;

import com.yqhp.mbg.po.TestSuite;
import com.yqhp.model.vo.TestSuiteVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestSuiteDao {

    List<TestSuiteVo> selectByTestSuite(TestSuite testSuite);
}
