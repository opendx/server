package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.dao.ProjectDao;
import com.yqhp.mbg.mapper.ProjectMapper;
import com.yqhp.mbg.po.Project;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.Page;
import com.yqhp.model.vo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Service
public class ProjectService extends BaseService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectDao projectDao;

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
        if (pageRequest.getPageNum() != null && pageRequest.getPageSize() != null) {
            //分页
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }
        List<ProjectVo> projectVos = projectDao.selectByProject(project);
        return Response.success(Page.convert(new PageInfo(projectVos)));
    }

}
