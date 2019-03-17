package com.yqhp.mbg.mapper;

import com.yqhp.mbg.po.Device;
import com.yqhp.mbg.po.DeviceExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DeviceMapper {
    long countByExample(DeviceExample example);

    int deleteByExample(DeviceExample example);

    int deleteByPrimaryKey(String id);

    int insert(Device record);

    int insertSelective(Device record);

    List<Device> selectByExample(DeviceExample example);

    Device selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Device record, @Param("example") DeviceExample example);

    int updateByExample(@Param("record") Device record, @Param("example") DeviceExample example);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
}