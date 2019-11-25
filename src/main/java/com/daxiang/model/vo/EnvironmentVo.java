package com.daxiang.model.vo;

import com.daxiang.mbg.po.Environment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class EnvironmentVo extends Environment {
    private String creatorNickName;

    public static EnvironmentVo convert(Environment environment, String creatorNickName) {
        EnvironmentVo environmentVo = new EnvironmentVo();
        BeanUtils.copyProperties(environment, environmentVo);
        environmentVo.setCreatorNickName(creatorNickName);
        return environmentVo;
    }
}
