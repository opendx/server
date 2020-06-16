package com.daxiang.controller;

import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.ProjectService;
import com.daxiang.mbg.po.Project;
import com.daxiang.validator.group.UpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    @DeleteMapping("/{projectId}")
    public Response delete(@PathVariable Integer projectId) {
        return projectService.delete(projectId);
    }

    /**
     * 修改项目
     */
    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Project project) {
        return projectService.update(project);
    }

    /**
     * 查询项目列表
     *
     * @return
     */
    @PostMapping("/list")
    public Response list(Project project, PageRequest pageRequest) {
        return projectService.list(project, pageRequest);
    }

}
