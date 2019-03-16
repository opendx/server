package com.yqhp.service;

import com.yqhp.mbg.mapper.UserMapper;
import com.yqhp.mbg.po.User;
import com.yqhp.mbg.po.UserExample;
import com.yqhp.model.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by jiangyitao.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录或注册
     *
     * @param user
     * @return
     */
    public Response loginOrRegister(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);

        if (CollectionUtils.isEmpty(users)) {
            if (StringUtils.isEmpty(user.getNickName())) {
                //没有填昵称，认为是想登录
                return Response.fail("账号或密码错误");
            }
            //有昵称，注册
            user.setCreateTime(new Date());
            try {
                int insertRow = userMapper.insertSelective(user);
                if (insertRow != 1) {
                    return Response.fail("注册失败，请稍后重试");
                }
            } catch (DuplicateKeyException e) {
                return Response.fail("用户名已存在");
            }
            return Response.success("注册成功", user);
        }
        return Response.success("登录成功", users.get(0));
    }
}
