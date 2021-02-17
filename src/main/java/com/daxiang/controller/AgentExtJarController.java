package com.daxiang.controller;

import com.daxiang.mbg.po.AgentExtJar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.AgentExtJarVo;
import com.daxiang.service.AgentExtJarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/agentExtJar")
public class AgentExtJarController {

    @Autowired
    private AgentExtJarService agentExtJarService;

    @PostMapping("/upload")
    public Response upload(MultipartFile file) {
        agentExtJarService.upload(file);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id) {
        agentExtJarService.delete(id);
        return Response.success("删除成功");
    }

    @PostMapping("/list")
    public Response list(AgentExtJar query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<AgentExtJarVo> pagedData = agentExtJarService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<AgentExtJarVo> appVos = agentExtJarService.getAgentExtJarVos(query, orderBy);
            return Response.success(appVos);
        }
    }

    @GetMapping("/lastUploadTimeList")
    public Response getLastUploadTimeList() {
        return Response.success(agentExtJarService.getLastUploadTimeList());
    }
}
