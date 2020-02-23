package com.daxiang.model.vo;

import com.daxiang.mbg.po.Environment;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class EnvironmentVo extends Environment {
    private String creatorNickName = "";
}
