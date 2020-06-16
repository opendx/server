package com.daxiang.service;

import com.daxiang.dao.UserProjectDao;
import com.daxiang.mbg.mapper.UserProjectMapper;
import com.daxiang.mbg.po.Project;
import com.daxiang.mbg.po.UserProject;
import com.daxiang.mbg.po.UserProjectExample;
import com.daxiang.model.dto.UserProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class UserProjectService {

    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private UserProjectDao userProjectDao;

    public int addBatch(List<UserProject> userProjects) {
        if (CollectionUtils.isEmpty(userProjects)) {
            return 0;
        }
        return userProjectDao.insertBatch(userProjects);
    }

    public int addBatch(Integer userId, List<Project> projects) {
        List<UserProject> userProjects = projects.stream().map(project -> {
            UserProject userProject = new UserProject();
            userProject.setUserId(userId);
            userProject.setProjectId(project.getId());
            return userProject;
        }).collect(Collectors.toList());
        return addBatch(userProjects);
    }

    public int deleteByUserId(Integer userId) {
        UserProjectExample example = new UserProjectExample();
        UserProjectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return userProjectMapper.deleteByExample(example);
    }

    public List<UserProjectDto> getUserProjectDtosByUserIds(List<Integer> userIds) {
        return userProjectDao.selectUserProjectDtosByUserIds(userIds);
    }
}
