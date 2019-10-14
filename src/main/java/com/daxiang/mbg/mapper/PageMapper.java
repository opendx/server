package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.Page;
import com.daxiang.mbg.po.PageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PageMapper {
    long countByExample(PageExample example);

    int deleteByExample(PageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Page record);

    int insertSelective(Page record);

    List<Page> selectByExampleWithBLOBs(PageExample example);

    List<Page> selectByExample(PageExample example);

    Page selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Page record, @Param("example") PageExample example);

    int updateByExampleWithBLOBs(@Param("record") Page record, @Param("example") PageExample example);

    int updateByExample(@Param("record") Page record, @Param("example") PageExample example);

    int updateByPrimaryKeySelective(Page record);

    int updateByPrimaryKeyWithBLOBs(Page record);

    int updateByPrimaryKey(Page record);
}