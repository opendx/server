package com.daxiang.model.vo;

import com.daxiang.mbg.po.Device;
import lombok.Data;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@Data
public class AgentVo {
    private String agentIp;
    private Integer agentPort;
    private List<Device> devices;
}
