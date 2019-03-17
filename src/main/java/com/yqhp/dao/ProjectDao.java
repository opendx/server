package com.yqhp.dao;

import com.yqhp.mbg.po.Project;
import com.yqhp.model.vo.ProjectVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface ProjectDao {
    List<ProjectVo> selectByProject(Project project);
}
