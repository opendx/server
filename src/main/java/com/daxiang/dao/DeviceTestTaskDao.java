package com.daxiang.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface DeviceTestTaskDao {
    int deleteInBatch(@Param("ids") List<Integer> ids);
}
