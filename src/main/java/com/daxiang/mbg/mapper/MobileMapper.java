package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.Mobile;
import com.daxiang.mbg.po.MobileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MobileMapper {
    long countByExample(MobileExample example);

    int deleteByExample(MobileExample example);

    int deleteByPrimaryKey(String id);

    int insert(Mobile record);

    int insertSelective(Mobile record);

    List<Mobile> selectByExample(MobileExample example);

    Mobile selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Mobile record, @Param("example") MobileExample example);

    int updateByExample(@Param("record") Mobile record, @Param("example") MobileExample example);

    int updateByPrimaryKeySelective(Mobile record);

    int updateByPrimaryKey(Mobile record);
}