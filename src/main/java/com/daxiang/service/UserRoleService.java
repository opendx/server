package com.daxiang.service;

import com.daxiang.dao.UserRoleDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.UserRoleMapper;
import com.daxiang.mbg.po.Role;
import com.daxiang.mbg.po.UserRole;
import com.daxiang.mbg.po.UserRoleExample;
import com.daxiang.model.dto.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    public void addBatch(List<UserRole> userRoles) {
        int insertCount = userRoleDao.insertBatch(userRoles);
        if (insertCount != userRoles.size()) {
            throw new ServerException("添加失败");
        }
    }

    public void addBatch(Integer userId, List<Role> roles) {
        List<UserRole> userRoles = roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(role.getId());
            return userRole;
        }).collect(Collectors.toList());

        addBatch(userRoles);
    }

    public int deleteByUserId(Integer userId) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return userRoleMapper.deleteByExample(example);
    }

    public List<UserRoleDto> getUserRoleDtosByUserIds(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }

        return userRoleDao.selectUserRoleDtosByUserIds(userIds);
    }
}
