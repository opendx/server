package com.daxiang.controller;

import com.daxiang.mbg.po.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.PageService;
import com.daxiang.validator.group.UpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return pageService.add(page);
    }

    @GetMapping("/{pageId}")
    public Response getPageVoById(@PathVariable Integer pageId) {
        return pageService.getPageVoById(pageId);
    }

    @DeleteMapping("/{pageId}")
    public Response delete(@PathVariable Integer pageId) {
        return pageService.delete(pageId);
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Page page) {
        return pageService.update(page);
    }


    @PostMapping("/list")
    public Response list(Page page, PageRequest pageRequest) {
        return pageService.list(page, pageRequest);
    }

}
