package com.daxiang.controller;

import com.daxiang.mbg.po.Environment;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.EnvironmentVo;
import com.daxiang.service.EnvironmentService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody Environment environment) {
        environmentService.add(environment);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{environmentId}")
    public Response delete(@PathVariable Integer environmentId) {
        environmentService.delete(environmentId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Environment environment) {
        environmentService.update(environment);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Environment query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<EnvironmentVo> pagedData = environmentService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<EnvironmentVo> environmentVos = environmentService.getEnvironmentVos(query, orderBy);
            return Response.success(environmentVos);
        }
    }
}