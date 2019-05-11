package com.fgnb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fgnb.mbg.po.Project;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 获取项目类型
     *
     * @return
     */
    @GetMapping("/types")
    public Response getProjectTypes() {
        JSONObject android = new JSONObject();
        android.put("type", Project.ANDROID_TYPE);
        android.put("name", "Android");

        JSONObject ios = new JSONObject();
        ios.put("type", Project.IOS_TYPE);
        ios.put("name", "iOS");

        JSONObject web = new JSONObject();
        web.put("type", Project.WEB_TYPE);
        web.put("name", "web");

        JSONArray projectTypes = new JSONArray();
        projectTypes.add(android);
        projectTypes.add(ios);
        projectTypes.add(web);

        return Response.success(projectTypes);
    }

    /**
     * 新增项目
     *
     * @return
     */
    @PostMapping("/add")
    public Response add(@Valid @RequestBody Project project) {
        return projectService.add(project);
    }

    /**
     * 删除项目
     *
     * @param projectId
     * @return
     */
    @GetMapping("/delete/{projectId}")
    public Response delete(@PathVariable Integer projectId) {
        return projectService.delete(projectId);
    }

    /**
     * 修改项目
     */
    @PostMapping("/update")
    public Response update(@Valid @RequestBody Project project) {
        return projectService.update(project);
    }


    /**
     * 查询项目列表
     * @return
     */
    @PostMapping("/list")
    public Response list(Project project, PageRequest pageRequest){
        return projectService.list(project,pageRequest);
    }

}
