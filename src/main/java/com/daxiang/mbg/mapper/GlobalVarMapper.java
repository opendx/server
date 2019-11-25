package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.mbg.po.GlobalVarExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GlobalVarMapper {
    long countByExample(GlobalVarExample example);

    int deleteByExample(GlobalVarExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GlobalVar record);

    int insertSelective(GlobalVar record);

    List<GlobalVar> selectByExampleWithBLOBs(GlobalVarExample example);

    List<GlobalVar> selectByExample(GlobalVarExample example);

    GlobalVar selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GlobalVar record, @Param("example") GlobalVarExample example);

    int updateByExampleWithBLOBs(@Param("record") GlobalVar record, @Param("example") GlobalVarExample example);

    int updateByExample(@Param("record") GlobalVar record, @Param("example") GlobalVarExample example);

    int updateByPrimaryKeySelective(GlobalVar record);

    int updateByPrimaryKeyWithBLOBs(GlobalVar record);

    int updateByPrimaryKey(GlobalVar record);
}