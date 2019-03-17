package com.yqhp.dao;

import com.yqhp.mbg.po.Device;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface DeviceDao {
    List<Device> selectByDevice(Device device);
}
