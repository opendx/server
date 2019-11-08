package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.TestPlan;
import com.daxiang.mbg.po.TestPlanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestPlanMapper {
    long countByExample(TestPlanExample example);

    int deleteByExample(TestPlanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestPlan record);

    int insertSelective(TestPlan record);

    List<TestPlan> selectByExampleWithBLOBs(TestPlanExample example);

    List<TestPlan> selectByExample(TestPlanExample example);

    TestPlan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestPlan record, @Param("example") TestPlanExample example);

    int updateByExampleWithBLOBs(@Param("record") TestPlan record, @Param("example") TestPlanExample example);

    int updateByExample(@Param("record") TestPlan record, @Param("example") TestPlanExample example);

    int updateByPrimaryKeySelective(TestPlan record);

    int updateByPrimaryKeyWithBLOBs(TestPlan record);

    int updateByPrimaryKey(TestPlan record);
}