package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.mapper.UserMapper;
import com.daxiang.utils.TokenUtil;
import com.daxiang.mbg.po.User;
import com.daxiang.mbg.po.UserExample;
import com.daxiang.model.Response;
import com.daxiang.model.UserCache;
import com.daxiang.model.vo.UserVo;
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

    public Response login(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        User query = new User();
        query.setUsername(user.getUsername());
        query.setPassword(user.getPassword());
        List<User> users = selectByUser(query);

        if (CollectionUtils.isEmpty(users)) {
            return Response.fail("账号或密码错误");
        } else {
            user = users.get(0);
            UserVo userVo = UserVo.convert(user, TokenUtil.create(user.getId() + ""));
            UserCache.add(user.getId(), user);
            return Response.success("登录成功", userVo);
        }
    }

    public Response register(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCreateTime(new Date());

        int insertRow;
        try {
            insertRow = userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return Response.fail("用户名已存在");
        }
        return insertRow == 1 ? Response.success("注册成功") : Response.fail("注册失败，请稍后重试");
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
        return Response.success();
    }

    public List<User> selectAll() {
        return selectByUser(null);
    }

    public List<User> selectByUser(User user) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        if (user != null) {
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
        }

        return userMapper.selectByExample(example);
    }
}
