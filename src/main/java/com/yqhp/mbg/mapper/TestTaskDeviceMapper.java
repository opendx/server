package com.yqhp.mbg.mapper;

import com.yqhp.mbg.po.TestTaskDevice;
import com.yqhp.mbg.po.TestTaskDeviceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestTaskDeviceMapper {
    long countByExample(TestTaskDeviceExample example);

    int deleteByExample(TestTaskDeviceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestTaskDevice record);

    int insertSelective(TestTaskDevice record);

    List<TestTaskDevice> selectByExampleWithBLOBs(TestTaskDeviceExample example);

    List<TestTaskDevice> selectByExample(TestTaskDeviceExample example);

    TestTaskDevice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestTaskDevice record, @Param("example") TestTaskDeviceExample example);

    int updateByExampleWithBLOBs(@Param("record") TestTaskDevice record, @Param("example") TestTaskDeviceExample example);

    int updateByExample(@Param("record") TestTaskDevice record, @Param("example") TestTaskDeviceExample example);

    int updateByPrimaryKeySelective(TestTaskDevice record);

    int updateByPrimaryKeyWithBLOBs(TestTaskDevice record);

    int updateByPrimaryKey(TestTaskDevice record);
}