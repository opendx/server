package com.daxiang.model.vo;

import com.daxiang.mbg.po.Project;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class ProjectVo extends Project {
    private String creatorNickName = "";
}
