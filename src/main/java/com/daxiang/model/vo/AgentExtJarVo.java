package com.daxiang.model.vo;

import com.daxiang.mbg.po.AgentExtJar;
import com.daxiang.utils.HttpServletUtil;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class AgentExtJarVo extends AgentExtJar {
    private String uploadorNickName = "";
    private String downloadUrl;

    public String getDownloadUrl() {
        return HttpServletUtil.getStaticResourceUrl(getFilePath());
    }
}
