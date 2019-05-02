package com.yqhp.agent;

import com.alibaba.fastjson.JSONObject;
import com.yqhp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jiangyitao.
 */
@Component
public class AgentApi {

    public static final String PROTOCOL_PREFIX = "http://";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 调试action
     *
     * @param agentIp
     * @param agentPort
     * @param requestBody
     * @return
     */
    public Response debugAction(String agentIp, int agentPort, JSONObject requestBody) {
        return restTemplate.postForObject(PROTOCOL_PREFIX + agentIp + ":" + agentPort + "/action/debug", requestBody, Response.class);
    }

    /**
     * 获取agent开启的webDrivers (chromeDriver ...)
     *
     * @param agentIp
     * @param agentPort
     * @return
     */
    public Response getSeleniumDrivers(String agentIp, int agentPort) {
        return restTemplate.getForObject(PROTOCOL_PREFIX + agentIp + ":" + agentPort + "/selenium/getDrivers", Response.class);
    }
}
