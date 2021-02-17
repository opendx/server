package com.daxiang.agent;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.Mobile;
import com.daxiang.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jiangyitao.
 */
@Component
public class AgentClient {

    @Autowired
    private RestTemplate restTemplate;

    public Response debugAction(String agentIp, int agentPort, JSONObject requestBody) {
        String url = getUrl(agentIp, agentPort, "/action/debug");
        return restTemplate.postForObject(url, requestBody, Response.class);
    }

    /**
     * dump apk信息
     *
     * @param agentIp
     * @param agentPort
     * @param apkDownloadUrl
     * @return
     */
    public Response aaptDumpBadging(String agentIp, int agentPort, String apkDownloadUrl) {
        String url = getUrl(agentIp, agentPort, "/android/aaptDumpBadging");
        return restTemplate.postForObject(url, apkDownloadUrl, Response.class);
    }

    public Response<Mobile> getMobile(String agentIp, int agentPort, String mobileId) {
        String url = getUrl(agentIp, agentPort, "/mobile/{mobileId}");
        return restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<Mobile>>() {
                },
                mobileId).getBody();
    }

    public Response<Mobile> deleteMobile(String agentIp, int agentPort, String mobileId) {
        String url = getUrl(agentIp, agentPort, "/mobile/{mobileId}");
        return restTemplate.exchange(url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Response<Mobile>>() {
                },
                mobileId).getBody();
    }

    public Response<Browser> getBrowser(String agentIp, int agentPort, String browserId) {
        String url = getUrl(agentIp, agentPort, "/browser/{browserId}");
        return restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<Browser>>() {
                },
                browserId).getBody();
    }

    public Response loadJar(String agentIp, int agentPort, String jarUrl) {
        String url = getUrl(agentIp, agentPort, "/agentExtJar/load");
        return restTemplate.postForObject(url, jarUrl, Response.class);
    }

    private String getUrl(String agentIp, int agentPort, String requestURI) {
        return String.format("http://%s:%d%s", agentIp, agentPort, requestURI);
    }
}
