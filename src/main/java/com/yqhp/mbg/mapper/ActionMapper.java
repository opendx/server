package com.yqhp.mbg.mapper;

import com.yqhp.mbg.po.Action;
import com.yqhp.mbg.po.ActionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ActionMapper {
    long countByExample(ActionExample example);

    int deleteByExample(ActionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Action record);

    int insertSelective(Action record);

    List<Action> selectByExampleWithBLOBs(ActionExample example);

    List<Action> selectByExample(ActionExample example);

    Action selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Action record, @Param("example") ActionExample example);

    int updateByExampleWithBLOBs(@Param("record") Action record, @Param("example") ActionExample example);

    int updateByExample(@Param("record") Action record, @Param("example") ActionExample example);

    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKeyWithBLOBs(Action record);

    int updateByPrimaryKey(Action record);
}