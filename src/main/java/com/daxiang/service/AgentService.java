package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.po.Device;
import com.daxiang.model.Response;
import com.daxiang.model.vo.AgentVo;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.Endpoint;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private RestTemplate restTemplate;

    public Response getOnlineAgents() {
        List<AgentVo> agentVos = getOnlineAgentsWithoutDevices();

        List<String> agentIps = agentVos.stream().map(AgentVo::getIp).collect(Collectors.toList());
        List<Device> devices = deviceService.getOnlineDevicesByAgentIps(agentIps);
        // agentIp: List<Device>
        Map<String, List<Device>> map = devices.stream().collect(Collectors.groupingBy(Device::getAgentIp));

        agentVos.forEach(agentVo -> agentVo.setDevices(map.get(agentVo.getIp())));
        return Response.success(agentVos);
    }

    public List<AgentVo> getOnlineAgentsWithoutDevices() {
        return getOnlineAgentStream().map(agent -> createAgentVoWithoutDevices(agent)).collect(Collectors.toList());
    }

    private Stream<Instance> getOnlineAgentStream() {
        return instanceRegistry.getInstances().collectList().block().stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus()));
    }

    private AgentVo createAgentVoWithoutDevices(Instance agent) {
        URI uri;
        try {
            uri = new URI(agent.getRegistration().getServiceUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        AgentVo agentVo = new AgentVo();
        agentVo.setIp(uri.getHost());
        agentVo.setPort(uri.getPort());

        Optional<Endpoint> env = agent.getEndpoints().get("env");
        if (env.isPresent()) {
            // http://agent_ip:agent_port/actuator/env
            JSONObject response = restTemplate.getForObject(env.get().getUrl(), JSONObject.class);
            Object sysProperties = response.getJSONArray("propertySources").stream()
                    .filter(property -> "systemProperties".equals(((JSONObject) property).get("name")))
                    .findFirst().get();
            JSONObject properties = ((JSONObject) (sysProperties)).getJSONObject("properties");

            agentVo.setOsName(properties.getJSONObject("os.name").getString("value"));
            agentVo.setJavaVersion(properties.getJSONObject("java.version").getString("value"));
            agentVo.setAppiumVersion(properties.getJSONObject("appiumVersion").getString("value"));
            agentVo.setIsConfigAapt(properties.getJSONObject("aapt").getBoolean("value"));
        }

        return agentVo;
    }
}
