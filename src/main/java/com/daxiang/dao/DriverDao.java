package com.daxiang.dao;

import com.daxiang.mbg.po.Driver;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface DriverDao {
    List<Driver> selectByTypeAndDeviceId(@Param("type") Integer type, @Param("deviceId") String deviceId);
}
