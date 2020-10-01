package com.daxiang.controller;

import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.ProjectVo;
import com.daxiang.service.ProjectService;
import com.daxiang.mbg.po.Project;
import com.daxiang.validator.group.UpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody Project project) {
        projectService.add(project);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{projectId}")
    public Response delete(@PathVariable Integer projectId) {
        projectService.delete(projectId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Project project) {
        projectService.update(project);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Project query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<ProjectVo> pagedData = projectService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<ProjectVo> projectVos = projectService.getProjectVos(query, orderBy);
            return Response.success(projectVos);
        }
    }
}
