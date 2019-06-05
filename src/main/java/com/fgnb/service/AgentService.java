package com.fgnb.service;

import com.fgnb.model.Response;
import com.fgnb.model.vo.AgentVo;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class AgentService {

    @Autowired
    private InstanceRegistry instanceRegistry;
    @Autowired
    private DeviceService deviceService;

    public Response listOfOnline() {
        List<Instance> agents = instanceRegistry.getInstances().collectList().block();

        List<AgentVo> agentVos = agents.stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus())) // 过滤出在线的agent
                .map(agent -> {
                    String url = agent.getRegistration().getServiceUrl(); // http://xx.xx.xx.x:xx/
                    String[] host = url.split("//")[1].split(":");

                    String agentIp = host[0];
                    Integer agentPort = Integer.parseInt(host[1].substring(0, host[1].length() - 1));

                    AgentVo agentVo = new AgentVo();
                    agentVo.setAgentIp(agentIp);
                    agentVo.setAgentPort(agentPort);
                    agentVo.setDevices(deviceService.getOnlineDevicesByAgentIp(agentIp));

                    return agentVo;
                }).collect(Collectors.toList());

        return Response.success(agentVos);
    }
}
