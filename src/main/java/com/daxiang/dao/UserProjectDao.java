package com.daxiang.dao;

import com.daxiang.mbg.po.UserProject;
import com.daxiang.model.dto.UserProjectDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface UserProjectDao {
    int insertBatch(@Param("userProjects") List<UserProject> userProjects);

    List<UserProjectDto> selectUserProjectDtosByUserIds(@Param("userIds") List<Integer> userIds);
}
