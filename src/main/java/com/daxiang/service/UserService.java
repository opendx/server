package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.UserMapper;
import com.daxiang.mbg.po.Project;
import com.daxiang.mbg.po.Role;
import com.daxiang.model.PagedData;
import com.daxiang.model.PageRequest;
import com.daxiang.model.dto.UserDto;
import com.daxiang.model.dto.UserProjectDto;
import com.daxiang.model.dto.UserRoleDto;
import com.daxiang.security.JwtTokenUtil;
import com.daxiang.mbg.po.User;
import com.daxiang.mbg.po.UserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void add(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());

        // 用户
        int insertCount;
        try {
            insertCount = userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw new ServerException(user.getUsername() + "已存在");
        }
        if (insertCount != 1) {
            throw new ServerException("添加用户失败，请稍后重试");
        }

        userRoleService.addBatch(user.getId(), userDto.getRoles());
        userProjectService.addBatch(user.getId(), userDto.getProjects());
    }

    @Transactional
    public void delete(Integer userId) {
        if (userId == null) {
            throw new ServerException("userId不能为空");
        }

        userRoleService.deleteByUserId(userId);
        userProjectService.deleteByUserId(userId);
        userMapper.deleteByPrimaryKey(userId);
    }

    @Transactional
    public void update(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        User originalUser = userMapper.selectByPrimaryKey(user.getId());
        if (originalUser == null) {
            throw new ServerException("用户不存在");
        }

        if (!originalUser.getPassword().equals(user.getPassword())) { // 修改了密码，需要重新encode
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        try {
            int updateCount = userMapper.updateByPrimaryKeySelective(user);
            if (updateCount != 1) {
                throw new ServerException("更新用户失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(user.getUsername() + "已存在");
        }

        userRoleService.deleteByUserId(user.getId());
        userRoleService.addBatch(user.getId(), userDto.getRoles());

        userProjectService.deleteByUserId(user.getId());
        userProjectService.addBatch(user.getId(), userDto.getProjects());
    }

    public PagedData<UserDto> list(User query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<UserDto> userDtos = getUserDtos(query, orderBy);
        return new PagedData<>(userDtos, page.getTotal());
    }

    private List<UserDto> convertUsersToUserDtos(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return new ArrayList<>();
        }

        List<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // 按用户id分组
        Map<Integer, List<UserRoleDto>> userRoleDtosMap = userRoleService.getUserRoleDtosByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(UserRoleDto::getUserId));
        Map<Integer, List<UserProjectDto>> userProjectDtosMap = userProjectService.getUserProjectDtosByUserIds(userIds).stream()
                .collect(Collectors.groupingBy(UserProjectDto::getUserId));

        List<UserDto> userDtos = users.stream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);

            List<UserRoleDto> userRoleDtos = userRoleDtosMap.get(user.getId());
            if (userRoleDtos == null) {
                userDto.setRoles(new ArrayList<>());
            } else {
                List<Role> roles = userRoleDtos.stream().map(UserRoleDto::getRole).collect(Collectors.toList());
                userDto.setRoles(roles);
            }

            List<UserProjectDto> userProjectDtos = userProjectDtosMap.get(user.getId());
            if (userProjectDtos == null) {
                userDto.setProjects(new ArrayList<>());
            } else {
                List<Project> projects = userProjectDtos.stream().map(UserProjectDto::getProject).collect(Collectors.toList());
                userDto.setProjects(projects);
            }

            return userDto;
        }).collect(Collectors.toList());

        return userDtos;
    }

    public String login(User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException e) {
            throw new ServerException(e.getMessage());
        }

        return JwtTokenUtil.createToken(user.getUsername());
    }

    public List<UserDto> getUserDtos(User query, String orderBy) {
        List<User> users = getUsers(query, orderBy);
        return convertUsersToUserDtos(users);
    }

    public List<User> getUsers(User query, String orderBy) {
        UserExample example = new UserExample();

        if (query != null) {
            UserExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getUsername())) {
                criteria.andUsernameLike("%" + query.getUsername() + "%");
            }
            if (!StringUtils.isEmpty(query.getNickName())) {
                criteria.andNickNameEqualTo(query.getNickName());
            }
            if (query.getStatus() != null) {
                criteria.andStatusEqualTo(query.getStatus());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return userMapper.selectByExample(example);
    }

    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public UserDto getUserDtoByUsername(String username) {
        User user = getUserByUsername(username);
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        List<Integer> singleUid = Collections.singletonList(user.getId());

        List<Role> roles = userRoleService.getUserRoleDtosByUserIds(singleUid).stream()
                .map(UserRoleDto::getRole).collect(Collectors.toList());
        userDto.setRoles(roles);

        boolean isAdmin = roles.stream().map(Role::getName).anyMatch("admin"::equals);
        if (isAdmin) {
            userDto.setProjects(projectService.getAll());
        } else {
            List<Project> projects = userProjectService.getUserProjectDtosByUserIds(singleUid).stream()
                    .map(UserProjectDto::getProject).collect(Collectors.toList());
            userDto.setProjects(projects);
        }

        return userDto;
    }

    public List<User> getUsersByIds(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(userIds);
        return userMapper.selectByExample(example);
    }

    public Map<Integer, User> getUserMapByIds(List<Integer> userIds) {
        List<User> users = getUsersByIds(userIds);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity(), (k1, k2) -> k1));
    }

    public User getUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}
