package com.daxiang.service;

import com.daxiang.dao.UserRoleDao;
import com.daxiang.mbg.mapper.UserRoleMapper;
import com.daxiang.mbg.po.Role;
import com.daxiang.mbg.po.UserRole;
import com.daxiang.mbg.po.UserRoleExample;
import com.daxiang.model.dto.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private UserRoleMapper userRoleMapper;


    public int insertInBatch(List<UserRole> userRoles) {
        return userRoleDao.insertInBatch(userRoles);
    }

    public int insert(Integer userId, List<Role> roles) {
        List<UserRole> userRoles = roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(role.getId());
            return userRole;
        }).collect(Collectors.toList());
        return insertInBatch(userRoles);
    }

    public int deleteByUserId(Integer userId) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return userRoleMapper.deleteByExample(example);
    }

    public List<UserRoleDto> selectUserRoleDtosByUserIds(List<Integer> userIds) {
        return userRoleDao.selectUserRoleDtosByUserIds(userIds);
    }
}
