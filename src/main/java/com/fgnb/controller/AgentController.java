package com.fgnb.controller;

import com.fgnb.model.Response;
import com.fgnb.service.AgentService;
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
