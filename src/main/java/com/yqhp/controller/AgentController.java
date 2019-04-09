package com.yqhp.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.yqhp.agent.AgentApi;
import com.yqhp.model.Response;
import com.yqhp.service.AgentService;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    /**
     * 获取当前在线的agent信息
     *
     * @return
     */
    @GetMapping("/listOfOnline")
    public Response listOfOnline() {
        return agentService.listOfOnline();
    }
}
