package com.fgnb.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fgnb.agent.AgentApi;
import com.fgnb.mbg.mapper.DeviceMapper;
import com.fgnb.mbg.po.Device;
import com.fgnb.mbg.po.DeviceExample;
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

                    //在线的设备
                    DeviceExample deviceExample = new DeviceExample();
                    deviceExample.createCriteria().andAgentIpEqualTo(agentIp).andStatusNotEqualTo(Device.OFFLINE_STATUS);
                    List<Device> onlineDevices = deviceMapper.selectByExample(deviceExample);
                    agentVo.setDevices(onlineDevices);

                    try {
                        Response response = agentApi.getSeleniumDrivers(agentIp, agentPort);
                        if(response.isSuccess()) {
                            List<AgentVo.Driver> drivers = JSON.parseArray(JSONArray.toJSONString(response.getData()), AgentVo.Driver.class);
                            agentVo.setDrivers(drivers);
                        }
                    } catch (Exception e) {
                        log.error("获取selenium drivers出错",e);
                    }

                    return agentVo;
                }).collect(Collectors.toList());

        return Response.success(agentVos);
    }
}
