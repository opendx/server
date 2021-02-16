package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.AgentExtJar;
import com.daxiang.mbg.po.AgentExtJarExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentExtJarMapper {
    long countByExample(AgentExtJarExample example);

    int deleteByExample(AgentExtJarExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AgentExtJar record);

    int insertSelective(AgentExtJar record);

    List<AgentExtJar> selectByExample(AgentExtJarExample example);

    AgentExtJar selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AgentExtJar record, @Param("example") AgentExtJarExample example);

    int updateByExample(@Param("record") AgentExtJar record, @Param("example") AgentExtJarExample example);

    int updateByPrimaryKeySelective(AgentExtJar record);

    int updateByPrimaryKey(AgentExtJar record);
}