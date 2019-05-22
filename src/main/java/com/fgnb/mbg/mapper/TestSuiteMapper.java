package com.fgnb.mbg.mapper;

import com.fgnb.mbg.po.TestSuite;
import com.fgnb.mbg.po.TestSuiteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestSuiteMapper {
    long countByExample(TestSuiteExample example);

    int deleteByExample(TestSuiteExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestSuite record);

    int insertSelective(TestSuite record);

    List<TestSuite> selectByExample(TestSuiteExample example);

    TestSuite selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestSuite record, @Param("example") TestSuiteExample example);

    int updateByExample(@Param("record") TestSuite record, @Param("example") TestSuiteExample example);

    int updateByPrimaryKeySelective(TestSuite record);

    int updateByPrimaryKey(TestSuite record);
}