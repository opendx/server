package com.yqhp.mbg.mapper;

import com.yqhp.mbg.po.TestTask;
import com.yqhp.mbg.po.TestTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestTaskMapper {
    long countByExample(TestTaskExample example);

    int deleteByExample(TestTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestTask record);

    int insertSelective(TestTask record);

    List<TestTask> selectByExample(TestTaskExample example);

    TestTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestTask record, @Param("example") TestTaskExample example);

    int updateByExample(@Param("record") TestTask record, @Param("example") TestTaskExample example);

    int updateByPrimaryKeySelective(TestTask record);

    int updateByPrimaryKey(TestTask record);
}