package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.Environment;
import com.daxiang.mbg.po.EnvironmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnvironmentMapper {
    long countByExample(EnvironmentExample example);

    int deleteByExample(EnvironmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Environment record);

    int insertSelective(Environment record);

    List<Environment> selectByExample(EnvironmentExample example);

    Environment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Environment record, @Param("example") EnvironmentExample example);

    int updateByExample(@Param("record") Environment record, @Param("example") EnvironmentExample example);

    int updateByPrimaryKeySelective(Environment record);

    int updateByPrimaryKey(Environment record);
}