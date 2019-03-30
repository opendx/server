package com.yqhp.service;

import com.yqhp.mbg.mapper.UserMapper;
import com.yqhp.mbg.po.User;
import com.yqhp.mbg.po.UserExample;
import com.yqhp.model.Response;
import com.yqhp.model.vo.UserVo;
import com.yqhp.utils.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
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
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userExample.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            //注册
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
        } else {
            //登录
            user = users.get(0);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setToken(TokenUtil.create(user.getId() + ""));
        ;

        return Response.success("登录成功", userVo);
    }
}
