package com.daxiang.service;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.Mobile;
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

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class AgentService {

    @Autowired
    private InstanceRegistry instanceRegistry;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private BrowserService browserService;
    @Autowired
    private RestTemplate restTemplate;

    public List<AgentVo> getOnlineAgents() {
        List<AgentVo> agentVos = getOnlineAgentsWithoutDevices();
        List<String> agentIps = agentVos.stream().map(AgentVo::getIp).collect(Collectors.toList());

        List<Mobile> mobiles = mobileService.getOnlineMobilesByAgentIps(agentIps);
        Map<String, List<Mobile>> mobileMap = mobiles.stream().collect(Collectors.groupingBy(Mobile::getAgentIp)); // agentIp: List<Mobile>

        List<Browser> browsers = browserService.getOnlineBrowsersByAgentIps(agentIps);
        Map<String, List<Browser>> browserMap = browsers.stream().collect(Collectors.groupingBy(Browser::getAgentIp)); // agentIp: List<Browser>

        agentVos.forEach(agentVo -> {
            String ip = agentVo.getIp();
            agentVo.setMobiles(mobileMap.get(ip));
            agentVo.setBrowsers(browserMap.get(ip));
        });

        return agentVos;
    }

    public List<AgentVo> getOnlineAgentsWithoutDevices() {
        return instanceRegistry.getInstances().collectList().block().stream()
                .filter(agent -> StatusInfo.STATUS_UP.equals(agent.getStatusInfo().getStatus()))
                .map(this::createAgentVoWithoutDevices)
                .collect(Collectors.toList());
    }

    private AgentVo createAgentVoWithoutDevices(Instance agent) {
        URI uri;
        try {
            uri = new URI(agent.getRegistration().getServiceUrl());
        } catch (URISyntaxException e) {
            throw new ServerException(e);
        }

        AgentVo agentVo = new AgentVo();
        agentVo.setInstanceId(agent.getId().getValue());
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
            agentVo.setIsConfigAapt(properties.getJSONObject("aapt").getBoolean("value"));
            agentVo.setVersion(properties.getJSONObject("agent.version").getString("value"));

            JSONObject appiumVersion = properties.getJSONObject("appium.version");
            if (appiumVersion != null) {
                agentVo.setAppiumVersion(appiumVersion.getString("value"));
            }
        }

        return agentVo;
    }
}
