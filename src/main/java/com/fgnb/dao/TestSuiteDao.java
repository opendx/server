package com.fgnb.dao;

import com.fgnb.mbg.po.TestSuite;
import com.fgnb.model.vo.TestSuiteVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface TestSuiteDao {

    List<TestSuiteVo> selectByTestSuite(TestSuite testSuite);
}
