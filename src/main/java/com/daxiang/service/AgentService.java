package com.daxiang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daxiang.agent.AgentApi;
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
import java.util.Optional;
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
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AgentApi agentApi;

    public Response listOfOnline() {
        List<Instance> agents = instanceRegistry.getInstances().collectList().block();
        List<AgentVo> agentVos = agents.stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus())) // 过滤出在线的agent
                .map(agent -> {
                    String url = agent.getRegistration().getServiceUrl(); // http://xx.xx.xx.x:xx/
                    URI uri;
                    try {
                        uri = new URI(url);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    String agentIp = uri.getHost();
                    Integer agentPort = uri.getPort();
                    String agentOsName = null;
                    String agentJavaVersion = null;
                    String agentAppiumVersion = null;

                    Response getAppiumVerisonResponse = agentApi.getAppiumVersion(agentIp, agentPort);
                    if (getAppiumVerisonResponse.isSuccess()) {
                        String data = JSONObject.toJSONString(getAppiumVerisonResponse.getData());
                        agentAppiumVersion = JSON.parseObject(data).getString("verison");
                    }

                    Optional<Endpoint> env = agent.getEndpoints().get("env");
                    if (env.isPresent()) {
                        // http://agent_ip:agent_port/actuator/env
                        JSONObject response = restTemplate.getForObject(env.get().getUrl(), JSONObject.class);
                        JSONArray propertySources = response.getJSONArray("propertySources");
                        JSONObject systemProperties = (JSONObject) (propertySources.stream().filter(property -> "systemProperties".equals(((JSONObject) property).get("name"))).findFirst().get());
                        JSONObject properties = systemProperties.getJSONObject("properties");
                        agentOsName = properties.getJSONObject("os.name").getString("value");
                        agentJavaVersion = properties.getJSONObject("java.version").getString("value");
                    }

                    AgentVo agentVo = new AgentVo();
                    agentVo.setIp(agentIp);
                    agentVo.setPort(agentPort);
                    agentVo.setOsName(agentOsName);
                    agentVo.setJavaVersion(agentJavaVersion);
                    agentVo.setAppiumVersion(agentAppiumVersion);
                    agentVo.setDevices(deviceService.getOnlineDevicesByAgentIp(agentIp));
                    return agentVo;
                }).collect(Collectors.toList());

        return Response.success(agentVos);
    }

    public List<AgentVo> getOnlineAgent() {
        return instanceRegistry.getInstances().collectList().block().stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus()))
                .map(agent -> {
                    String url = agent.getRegistration().getServiceUrl(); // http://xx.xx.xx.x:xx/
                    URI uri;
                    try {
                        uri = new URI(url);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    AgentVo agentVo = new AgentVo();
                    agentVo.setIp(uri.getHost());
                    agentVo.setPort(uri.getPort());
                    return agentVo;
                }).collect(Collectors.toList());
    }
}
