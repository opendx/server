package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.Driver;
import com.daxiang.mbg.po.DriverExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DriverMapper {
    long countByExample(DriverExample example);

    int deleteByExample(DriverExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Driver record);

    int insertSelective(Driver record);

    List<Driver> selectByExampleWithBLOBs(DriverExample example);

    List<Driver> selectByExample(DriverExample example);

    Driver selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Driver record, @Param("example") DriverExample example);

    int updateByExampleWithBLOBs(@Param("record") Driver record, @Param("example") DriverExample example);

    int updateByExample(@Param("record") Driver record, @Param("example") DriverExample example);

    int updateByPrimaryKeySelective(Driver record);

    int updateByPrimaryKeyWithBLOBs(Driver record);

    int updateByPrimaryKey(Driver record);
}