package com.daxiang.controller;

import com.daxiang.model.Response;
import com.daxiang.model.vo.AgentVo;
import com.daxiang.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/online")
    public Response getOnlineAgents() {
        List<AgentVo> agents = agentService.getOnlineAgents();
        return Response.success(agents);
    }

}
