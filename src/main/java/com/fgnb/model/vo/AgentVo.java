package com.fgnb.model.vo;

import com.fgnb.mbg.po.Device;
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
