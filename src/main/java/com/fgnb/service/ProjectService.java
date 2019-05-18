package com.fgnb.service;

import com.fgnb.mbg.po.ProjectExample;
import com.fgnb.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.ProjectMapper;
import com.fgnb.mbg.po.Project;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.Page;
import com.fgnb.model.vo.ProjectVo;
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

        try {
            int insertRow = projectMapper.insertSelective(project);
            if (insertRow != 1) {
                return Response.fail("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("添加项目成功");
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
        if (deleteRow != 1) {
            return Response.fail("删除失败，请确认项目是否存在");
        }

        return Response.success("删除成功");
    }

    /**
     * 更新项目
     *
     * @param project
     */
    public Response update(Project project) {
        if (project.getId() == null) {
            return Response.fail("项目id不能为空");
        }

        try {
            int updateRow = projectMapper.updateByPrimaryKeySelective(project);
            if (updateRow != 1) {
                return Response.fail("修改失败,请刷新重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新成功");
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
