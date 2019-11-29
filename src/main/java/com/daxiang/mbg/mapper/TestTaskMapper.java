package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.TestTask;
import com.daxiang.mbg.po.TestTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestTaskMapper {
    long countByExample(TestTaskExample example);

    int deleteByExample(TestTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestTask record);

    int insertSelective(TestTask record);

    List<TestTask> selectByExampleWithBLOBs(TestTaskExample example);

    List<TestTask> selectByExample(TestTaskExample example);

    TestTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestTask record, @Param("example") TestTaskExample example);

    int updateByExampleWithBLOBs(@Param("record") TestTask record, @Param("example") TestTaskExample example);

    int updateByExample(@Param("record") TestTask record, @Param("example") TestTaskExample example);

    int updateByPrimaryKeySelective(TestTask record);

    int updateByPrimaryKeyWithBLOBs(TestTask record);

    int updateByPrimaryKey(TestTask record);
}