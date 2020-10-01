package com.daxiang.controller;

import com.daxiang.mbg.po.Browser;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.service.BrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/browser")
public class BrowserController {

    @Autowired
    private BrowserService browserService;

    @PostMapping("/save")
    public Response save(@RequestBody @Valid Browser browser) {
        browserService.save(browser);
        return Response.success("保存成功");
    }

    @PostMapping("/list")
    public Response list(Browser query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<Browser> pagedData = browserService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<Browser> browsers = browserService.getBrowsers(query, orderBy);
            return Response.success(browsers);
        }
    }

    @GetMapping("/{browserId}/start")
    public Response start(@PathVariable String browserId) {
        Browser browser = browserService.start(browserId);
        return Response.success(browser);
    }

    @GetMapping("/online")
    public Response getOnlineBrowsers() {
        List<Browser> onlineBrowsers = browserService.getOnlineBrowsers();
        return Response.success(onlineBrowsers);
    }

}