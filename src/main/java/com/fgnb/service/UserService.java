package com.fgnb.service;

import com.alibaba.fastjson.JSONObject;
import com.fgnb.mbg.mapper.UserMapper;
import com.fgnb.mbg.po.User;
import com.fgnb.mbg.po.UserExample;
import com.fgnb.model.Response;
import com.fgnb.model.UserCache;
import com.fgnb.model.vo.UserVo;
import com.fgnb.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by jiangyitao.
 */
@Service
public class UserService extends BaseService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录或注册
     *
     * @param user
     * @return
     */
    public Response loginOrRegister(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        User query = new User();
        query.setUsername(user.getUsername());
        query.setPassword(user.getPassword());
        List<User> users = selectByUser(query);

        if (CollectionUtils.isEmpty(users)) { // 根据账号密码没查到用户信息
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
            // 根据账号密码有查到用户
            user = users.get(0);
        }

        UserVo userVo = UserVo.convert(user,TokenUtil.create(user.getId() + ""));
        UserCache.add(user.getId(), user);
        return Response.success("登录成功", userVo);
    }

    public Response getInfo() {
        User user = UserCache.getById(getUid());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", user.getNickName());
        jsonObject.put("avatar", "");
        jsonObject.put("introduction", "");
        jsonObject.put("roles", Arrays.asList("admin"));

        return Response.success(jsonObject);
    }

    public Response logout() {
        return Response.success("ok");
    }

    public List<User> selectAll() {
        return selectByUser(null);
    }

    public List<User> selectByUser(User user) {
        if (user == null) {
            user = new User();
        }
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if (user.getId() != null) {
            criteria.andIdEqualTo(user.getId());
        }
        if (!StringUtils.isEmpty(user.getUsername())) {
            criteria.andUsernameEqualTo(user.getUsername());
        }
        if (!StringUtils.isEmpty(user.getPassword())) {
            criteria.andPasswordEqualTo(user.getPassword());
        }
        if (!StringUtils.isEmpty(user.getNickName())) {
            criteria.andNickNameEqualTo(user.getNickName());
        }
        return userMapper.selectByExample(userExample);
    }
}
