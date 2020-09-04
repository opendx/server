package com.daxiang.service;

import com.daxiang.mbg.mapper.ProjectMapper;
import com.daxiang.mbg.po.User;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.mbg.po.ProjectExample;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.po.Project;
import com.daxiang.model.Page;
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

    public Response add(Project project) {
        project.setCreateTime(new Date());
        project.setCreatorUid(SecurityUtil.getCurrentUserId());

        int insertRow;
        try {
            insertRow = projectMapper.insertSelective(project);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加Project成功") : Response.fail("添加Project失败，请稍后重试");
    }

    public Response delete(Integer projectId) {
        if (projectId == null) {
            return Response.fail("项目id不能为空");
        }

        int deleteRow = projectMapper.deleteByPrimaryKey(projectId);
        return deleteRow == 1 ? Response.success("删除Project成功") : Response.fail("删除Project失败，请稍后重试");
    }

    public Response update(Project project) {
        int updateRow;
        try {
            updateRow = projectMapper.updateByPrimaryKeySelective(project);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新Project成功") : Response.fail("修改Project失败,请稍后重试");
    }

    public Response list(Project project, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Project> projects = selectByProject(project);
        List<ProjectVo> projectVos = convertProjectsToProjectVos(projects);

        if (needPaging) {
            long total = Page.getTotal(projects);
            return Response.success(Page.build(projectVos, total));
        } else {
            return Response.success(projectVos);
        }
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

        return projects.stream().map(project -> {
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
    }

    private List<Project> selectByProject(Project project) {
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();

        if (project != null) {
            if (project.getId() != null) {
                criteria.andIdEqualTo(project.getId());
            }
            if (!StringUtils.isEmpty(project.getName())) {
                criteria.andNameEqualTo(project.getName());
            }
            if (project.getPlatform() != null) {
                criteria.andPlatformEqualTo(project.getPlatform());
            }
        }
        example.setOrderByClause("create_time desc");

        return projectMapper.selectByExampleWithBLOBs(example);
    }

    public Project getProjectById(Integer id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    public List<Project> getAll() {
        return projectMapper.selectByExample(null);
    }
}
