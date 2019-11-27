package com.daxiang.service;

import com.daxiang.mbg.mapper.ProjectMapper;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.mbg.po.ProjectExample;
import com.daxiang.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.po.Project;
import com.daxiang.model.Page;
import com.daxiang.model.vo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class ProjectService extends BaseService {

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 新增项目
     *
     * @param project
     * @return
     */
    public Response add(Project project) {
        project.setCreateTime(new Date());
        project.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = projectMapper.insertSelective(project);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加Project成功") : Response.fail("添加Project失败，请稍后重试");
    }

    /**
     * 删除项目
     *
     * @param projectId
     */
    public Response delete(Integer projectId) {
        if (projectId == null) {
            return Response.fail("项目id不能为空");
        }

        int deleteRow = projectMapper.deleteByPrimaryKey(projectId);
        return deleteRow == 1 ? Response.success("删除Project成功") : Response.fail("删除Project失败，请稍后重试");
    }

    /**
     * 更新项目
     *
     * @param project
     */
    public Response update(Project project) {
        int updateRow;
        try {
            updateRow = projectMapper.updateByPrimaryKeySelective(project);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新Project成功") : Response.fail("修改Project失败,请稍后重试");
    }


    /**
     * 查询项目列表
     *
     * @param project
     * @param pageRequest
     * @return
     */
    public Response list(Project project, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Project> projects = selectByProject(project);
        List<ProjectVo> projectVos = projects.stream().map(p -> ProjectVo.convert(p, UserCache.getNickNameById(p.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用projects计算total
            long total = Page.getTotal(projects);
            return Response.success(Page.build(projectVos, total));
        } else {
            return Response.success(projectVos);
        }
    }

    public List<Project> selectByProject(Project project) {
        if (project == null) {
            project = new Project();
        }

        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();

        if (project.getId() != null) {
            criteria.andIdEqualTo(project.getId());
        }
        if (!StringUtils.isEmpty(project.getName())) {
            criteria.andNameEqualTo(project.getName());
        }
        if (project.getPlatform() != null) {
            criteria.andPlatformEqualTo(project.getPlatform());
        }
        projectExample.setOrderByClause("create_time desc");

        return projectMapper.selectByExample(projectExample);
    }
}
