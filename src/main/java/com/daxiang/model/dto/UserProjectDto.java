package com.daxiang.model.dto;

import com.daxiang.mbg.po.Project;
import com.daxiang.mbg.po.UserProject;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class UserProjectDto extends UserProject {
    private Project project;
}
