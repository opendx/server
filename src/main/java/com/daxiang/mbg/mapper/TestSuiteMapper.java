package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.TestSuite;
import com.daxiang.mbg.po.TestSuiteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestSuiteMapper {
    long countByExample(TestSuiteExample example);

    int deleteByExample(TestSuiteExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestSuite record);

    int insertSelective(TestSuite record);

    List<TestSuite> selectByExampleWithBLOBs(TestSuiteExample example);

    List<TestSuite> selectByExample(TestSuiteExample example);

    TestSuite selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestSuite record, @Param("example") TestSuiteExample example);

    int updateByExampleWithBLOBs(@Param("record") TestSuite record, @Param("example") TestSuiteExample example);

    int updateByExample(@Param("record") TestSuite record, @Param("example") TestSuiteExample example);

    int updateByPrimaryKeySelective(TestSuite record);

    int updateByPrimaryKeyWithBLOBs(TestSuite record);

    int updateByPrimaryKey(TestSuite record);
}