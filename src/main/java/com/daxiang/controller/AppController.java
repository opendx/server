package com.daxiang.controller;

import com.daxiang.mbg.po.App;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.AppVo;
import com.daxiang.service.AppService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/upload")
    public Response upload(@Valid App app, MultipartFile file) {
        appService.upload(app, file);
        return Response.success("上传成功");
    }

    @DeleteMapping("/{appId}")
    public Response delete(@PathVariable Integer appId) {
        appService.deleteAndClearRelatedRes(appId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody App app) {
        appService.update(app);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(App query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<AppVo> pagedData = appService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<AppVo> appVos = appService.getAppVos(query, orderBy);
            return Response.success(appVos);
        }
    }

    @GetMapping("/{appId}/aaptDumpBadging")
    public Response aaptDumpBadging(@PathVariable Integer appId) {
        appService.aaptDumpBadging(appId);
        return Response.success("获取成功");
    }

}
