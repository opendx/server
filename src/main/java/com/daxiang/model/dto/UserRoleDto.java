package com.daxiang.model.dto;

import com.daxiang.mbg.po.Role;
import com.daxiang.mbg.po.UserRole;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class UserRoleDto extends UserRole {
    private Role role;
}
