package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.BrowserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BrowserMapper {
    long countByExample(BrowserExample example);

    int deleteByExample(BrowserExample example);

    int deleteByPrimaryKey(String id);

    int insert(Browser record);

    int insertSelective(Browser record);

    List<Browser> selectByExample(BrowserExample example);

    Browser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Browser record, @Param("example") BrowserExample example);

    int updateByExample(@Param("record") Browser record, @Param("example") BrowserExample example);

    int updateByPrimaryKeySelective(Browser record);

    int updateByPrimaryKey(Browser record);
}