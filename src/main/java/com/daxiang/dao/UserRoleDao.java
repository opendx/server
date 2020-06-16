package com.daxiang.dao;

import com.daxiang.mbg.po.UserRole;
import com.daxiang.model.dto.UserRoleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface UserRoleDao {
    int insertBatch(@Param("userRoles") List<UserRole> userRoles);

    List<UserRoleDto> selectUserRoleDtosByUserIds(@Param("userIds") List<Integer> userIds);
}
