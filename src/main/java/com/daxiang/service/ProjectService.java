package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.ProjectMapper;
import com.daxiang.mbg.po.User;
import com.daxiang.model.PageRequest;
import com.daxiang.mbg.po.ProjectExample;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.mbg.po.Project;
import com.daxiang.model.PagedData;
import com.daxiang.model.vo.ProjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserService userService;

    public void add(Project project) {
        project.setCreateTime(new Date());
        project.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = projectMapper.insertSelective(project);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(project.getName() + "已存在");
        }
    }

    public void delete(Integer projectId) {
        if (projectId == null) {
            throw new ServerException("projectId不能为空");
        }

        int deleteCount = projectMapper.deleteByPrimaryKey(projectId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(Project project) {
        try {
            int updateCount = projectMapper.updateByPrimaryKeySelective(project);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(project.getName() + "已存在");
        }
    }

    public PagedData<ProjectVo> list(Project query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<ProjectVo> projectVos = getProjectVos(query, orderBy);
        return new PagedData<>(projectVos, page.getTotal());
    }

    private List<ProjectVo> convertProjectsToProjectVos(List<Project> projects) {
        if (CollectionUtils.isEmpty(projects)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = projects.stream()
                .map(Project::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        List<ProjectVo> projectVos = projects.stream().map(project -> {
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(project, projectVo);

            if (project.getCreatorUid() != null) {
                User user = userMap.get(project.getCreatorUid());
                if (user != null) {
                    projectVo.setCreatorNickName(user.getNickName());
                }
            }

            return projectVo;
        }).collect(Collectors.toList());

        return projectVos;
    }

    public List<ProjectVo> getProjectVos(Project query, String orderBy) {
        List<Project> projects = getProjects(query, orderBy);
        return convertProjectsToProjectVos(projects);
    }

    public List<Project> getProjects(Project query, String orderBy) {
        ProjectExample example = new ProjectExample();

        if (query != null) {
            ProjectExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
            if (query.getPlatform() != null) {
                criteria.andPlatformEqualTo(query.getPlatform());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return projectMapper.selectByExampleWithBLOBs(example);
    }

    public Project getProjectById(Integer id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    public List<Project> getAll() {
        return projectMapper.selectByExample(null);
    }
}
