package com.daxiang.model.vo;

import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.Mobile;
import lombok.Data;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@Data
public class AgentVo {
    private String instanceId;
    private String version;
    private String ip;
    private Integer port;
    private String osName;
    private String javaVersion;
    private String appiumVersion;
    private Boolean isConfigAapt;
    private List<Mobile> mobiles;
    private List<Browser> browsers;
}
