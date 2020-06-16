package com.daxiang.service;

import com.daxiang.mbg.mapper.RoleMapper;
import com.daxiang.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiangyitao.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Response list() {
        // select all
        return Response.success(roleMapper.selectByExample(null));
    }

}
