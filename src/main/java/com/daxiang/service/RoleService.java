package com.daxiang.service;

import com.daxiang.mbg.mapper.RoleMapper;
import com.daxiang.mbg.po.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<Role> list() {
        return roleMapper.selectByExample(null);
    }

}
