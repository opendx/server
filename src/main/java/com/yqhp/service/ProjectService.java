package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.mbg.mapper.ProjectMapper;
import com.yqhp.mbg.po.Project;
import com.yqhp.mbg.po.ProjectExample;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

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
        //分页排序
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), "create_time desc");
        //动态where
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();
        if (!StringUtils.isEmpty(project.getName())) {
            criteria.andNameEqualTo(project.getName());
        }
        if (project.getType() != null) {
            criteria.andTypeEqualTo(project.getType());
        }
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return Response.success(PageVo.convert(new PageInfo(projects)));
    }


}
