package com.fgnb.dao;

import com.fgnb.mbg.po.Device;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface DeviceDao {
    List<Device> selectByDevice(Device device);
}
