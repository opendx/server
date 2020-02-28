package com.daxiang.service;

import com.daxiang.dao.UserDao;
import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.UserMapper;
import com.daxiang.mbg.po.Project;
import com.daxiang.mbg.po.Role;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.dto.UserDto;
import com.daxiang.model.dto.UserProjectDto;
import com.daxiang.model.dto.UserRoleDto;
import com.daxiang.security.JwtTokenUtil;
import com.daxiang.mbg.po.User;
import com.daxiang.mbg.po.UserExample;
import com.daxiang.model.Response;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Response add(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());

        // 用户
        int insertRow;
        try {
            insertRow = userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return Response.fail("用户名已存在");
        }
        if (insertRow != 1) {
            return Response.fail("添加用户失败，请稍后重试");
        }

        // 用户角色
        insertRow = userRoleService.insert(user.getId(), userDto.getRoles());
        if (insertRow != userDto.getRoles().size()) {
            throw new BusinessException("添加角色失败，请稍后重试");
        }

        // 用户项目
        insertRow = userProjectService.insert(user.getId(), userDto.getProjects());
        if (insertRow != userDto.getProjects().size()) {
            throw new BusinessException("添加用户项目失败，请稍后重试");
        }

        return Response.success("添加用户成功");
    }


    @Transactional
    public Response delete(Integer userId) {
        Assert.notNull(userId, "userId不能为空");

        // 删除用户角色
        userRoleService.deleteByUserId(userId);
        // 删除用户项目
        userProjectService.deleteByUserId(userId);
        // 删除用户
        userMapper.deleteByPrimaryKey(userId);

        return Response.success("删除成功");
    }

    @Transactional
    public Response update(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        User originalUser = userMapper.selectByPrimaryKey(user.getId());
        if (originalUser == null) {
            return Response.fail("用户不存在");
        }

        if (!originalUser.getPassword().equals(user.getPassword())) { // 修改了密码，需要重新encode
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        int updateRow;
        try {
            updateRow = userMapper.updateByPrimaryKeySelective(user);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        if (updateRow != 1) {
            return Response.fail("更新用户失败，请稍后重试");
        }

        // 删除用户角色
        userRoleService.deleteByUserId(user.getId());

        // 添加用户角色
        int insertRow = userRoleService.insert(user.getId(), userDto.getRoles());
        if (insertRow != userDto.getRoles().size()) {
            throw new BusinessException("添加用户角色失败");
        }

        // 删除用户项目
        userProjectService.deleteByUserId(user.getId());

        // 添加用户项目
        insertRow = userProjectService.insert(user.getId(), userDto.getProjects());
        if (insertRow != userDto.getProjects().size()) {
            throw new BusinessException("添加用户项目失败");
        }

        return Response.success("更新用户成功");
    }

    public Response list(User user, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<User> users = selectByUser(user);
        List<UserDto> userDtos = convertUsersToUserDtos(users);

        if (needPaging) {
            return Response.success(Page.build(userDtos, Page.getTotal(users)));
        } else {
            return Response.success(userDtos);
        }
    }

    private List<UserDto> convertUsersToUserDtos(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // 按用户id分组
        Map<Integer, List<UserRoleDto>> userRoleDtosMap = userRoleService.selectUserRoleDtosByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleDto::getUserId));
        Map<Integer, List<UserProjectDto>> userProjectDtosMap = userProjectService.selectUserProjectDtosByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(UserProjectDto::getUserId));


        return users.stream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);

            List<UserRoleDto> userRoleDtos = userRoleDtosMap.get(user.getId());
            if (userRoleDtos == null) {
                userDto.setRoles(Collections.EMPTY_LIST);
            } else {
                List<Role> roles = userRoleDtos.stream().map(UserRoleDto::getRole).collect(Collectors.toList());
                userDto.setRoles(roles);
            }

            List<UserProjectDto> userProjectDtos = userProjectDtosMap.get(user.getId());
            if (userProjectDtos == null) {
                userDto.setProjects(Collections.EMPTY_LIST);
            } else {
                List<Project> projects = userProjectDtos.stream().map(UserProjectDto::getProject).collect(Collectors.toList());
                userDto.setProjects(projects);
            }

            return userDto;
        }).collect(Collectors.toList());
    }

    public Response login(User user) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (DisabledException e) {
            return Response.fail("账户已禁用");
        } catch (AuthenticationException e) {
            return Response.fail("用户名或密码错误");
        }

        String token = JwtTokenUtil.createToken(user.getUsername());
        return Response.success("登陆成功", ImmutableMap.of("token", token));
    }

    public Response getInfo() {
        return Response.success(SecurityUtil.getCurrentUserDto());
    }

    public Response logout() {
        return Response.success();
    }

    public List<User> selectByUser(User user) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getId() != null) {
                criteria.andIdEqualTo(user.getId());
            }
            if (!StringUtils.isEmpty(user.getUsername())) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (!StringUtils.isEmpty(user.getNickName())) {
                criteria.andNickNameEqualTo(user.getNickName());
            }
            if (user.getStatus() != null) {
                criteria.andStatusEqualTo(user.getStatus());
            }
        }
        example.setOrderByClause("create_time desc");

        return userMapper.selectByExample(example);
    }

    public UserDto selectUserDtoByUsername(String useranme) {
        return userDao.selectUserDtoByUsername(useranme);
    }

    public List<User> selectByUserIds(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.EMPTY_LIST;
        }

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(userIds);
        return userMapper.selectByExample(example);
    }

    public Map<Integer, User> getUserMapByUserIds(List<Integer> userIds) {
        List<User> users = selectByUserIds(userIds);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.EMPTY_MAP;
        }

        return users.stream().collect(Collectors.toMap(User::getId, u -> u, (k1, k2) -> k1));
    }

    public User selectByPrimaryKey(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}
