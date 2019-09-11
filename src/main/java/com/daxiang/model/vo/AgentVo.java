package com.daxiang.model.vo;

import com.daxiang.mbg.po.Device;
import lombok.Data;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@Data
public class AgentVo {
    private String ip;
    private Integer port;
    private String osName;
    private String javaVersion;
    private String appiumVersion;
    private Boolean isConfigAapt;
    private List<Device> devices;
}
