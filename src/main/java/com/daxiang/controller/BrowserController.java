package com.daxiang.controller;

import com.daxiang.mbg.po.Browser;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.BrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return browserService.save(browser);
    }

    @PostMapping("/list")
    public Response list(Browser browser, PageRequest pageRequest) {
        return browserService.list(browser, pageRequest);
    }

    @GetMapping("/{browserId}/start")
    public Response start(@PathVariable String browserId) {
        return browserService.start(browserId);
    }

    @GetMapping("/online")
    public Response getOnlineBrowsers() {
        return browserService.getOnlineBrowsers();
    }

}
