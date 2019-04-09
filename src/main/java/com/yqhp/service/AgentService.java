package com.yqhp.service;

import com.yqhp.agent.AgentApi;
import com.yqhp.mbg.mapper.DeviceMapper;
import com.yqhp.model.Response;
import com.yqhp.model.vo.AgentVo;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class AgentService {
    @Autowired
    private InstanceRegistry instanceRegistry;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private AgentApi agentApi;

    public Response listOfOnline() {
        List<Instance> agents = instanceRegistry.getInstances().collectList().block();

        List<AgentVo> agentVos = agents.stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus())) //过滤出在线的agent
                .map(agent -> {
                    String url = agent.getRegistration().getServiceUrl(); // http://xx.xx.xx.x:xx/
                    String[] host = url.split("//")[1].split(":");

                    String agentIp = host[0];
                    Integer agentPort = Integer.parseInt(host[1].substring(0, host[1].length() - 1));

                    AgentVo agentVo = new AgentVo();
                    agentVo.setAgentIp(agentIp);
                    agentVo.setAgentPort(agentPort);

                    //todo
                    return agentVo;
                }).collect(Collectors.toList());

        return Response.success(agentVos);
    }
}
