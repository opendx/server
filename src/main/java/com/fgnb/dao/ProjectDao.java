package com.fgnb.dao;

import com.fgnb.mbg.po.Project;
import com.fgnb.model.vo.ProjectVo;

import java.util.List;

/**
 * Created by jiangyitao.
 */
public interface ProjectDao {
    List<ProjectVo> selectByProject(Project project);
}
