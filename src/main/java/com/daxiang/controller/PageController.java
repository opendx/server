package com.daxiang.controller;

import com.daxiang.mbg.po.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.PageVo;
import com.daxiang.service.PageService;
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
@Slf4j
@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody Page page) {
        pageService.add(page);
        return Response.success("添加成功", page);
    }

    @GetMapping("/{pageId}")
    public Response getPageVoById(@PathVariable Integer pageId) {
        PageVo pageVo = pageService.getPageVoById(pageId);
        return Response.success(pageVo);
    }

    @DeleteMapping("/{pageId}")
    public Response delete(@PathVariable Integer pageId) {
        pageService.deleteAndClearRelatedRes(pageId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Page page) {
        pageService.update(page);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Page query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<PageVo> pagedData = pageService.listWithoutWindowHierarchy(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<PageVo> pageVos = pageService.getPageVosWithoutWindowHierarchy(query, orderBy);
            return Response.success(pageVos);
        }
    }

}
