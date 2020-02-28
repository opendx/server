package com.daxiang.mbg.mapper;

import com.daxiang.mbg.po.UserProject;
import com.daxiang.mbg.po.UserProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserProjectMapper {
    long countByExample(UserProjectExample example);

    int deleteByExample(UserProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserProject record);

    int insertSelective(UserProject record);

    List<UserProject> selectByExample(UserProjectExample example);

    UserProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserProject record, @Param("example") UserProjectExample example);

    int updateByExample(@Param("record") UserProject record, @Param("example") UserProjectExample example);

    int updateByPrimaryKeySelective(UserProject record);

    int updateByPrimaryKey(UserProject record);
}