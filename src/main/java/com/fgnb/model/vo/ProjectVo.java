package com.fgnb.model.vo;

import com.fgnb.mbg.po.Project;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class ProjectVo extends Project {
    private String creatorNickName;

    public static ProjectVo convert(Project project,String creatorNickName) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        projectVo.setCreatorNickName(creatorNickName);
        return projectVo;
    }
}
