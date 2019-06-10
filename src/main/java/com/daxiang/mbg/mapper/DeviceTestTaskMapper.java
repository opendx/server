package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.mbg.po.DeviceTestTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceTestTaskMapper {
    long countByExample(DeviceTestTaskExample example);

    int deleteByExample(DeviceTestTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceTestTask record);

    int insertSelective(DeviceTestTask record);

    List<DeviceTestTask> selectByExampleWithBLOBs(DeviceTestTaskExample example);

    List<DeviceTestTask> selectByExample(DeviceTestTaskExample example);

    DeviceTestTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DeviceTestTask record, @Param("example") DeviceTestTaskExample example);

    int updateByExampleWithBLOBs(@Param("record") DeviceTestTask record, @Param("example") DeviceTestTaskExample example);

    int updateByExample(@Param("record") DeviceTestTask record, @Param("example") DeviceTestTaskExample example);

    int updateByPrimaryKeySelective(DeviceTestTask record);

    int updateByPrimaryKeyWithBLOBs(DeviceTestTask record);

    int updateByPrimaryKey(DeviceTestTask record);
}