package com.daxiang.controller;

import com.daxiang.mbg.po.Mobile;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/mobile")
public class MobileController {

    @Autowired
    private MobileService mobileService;

    @PostMapping("/save")
    public Response save(@RequestBody @Valid Mobile mobile) {
        return mobileService.save(mobile);
    }

    @PostMapping("/list")
    public Response list(Mobile mobile, PageRequest pageRequest) {
        return mobileService.list(mobile, pageRequest);
    }

    @GetMapping("/{mobileId}/start")
    public Response start(@PathVariable String mobileId) {
        return mobileService.start(mobileId);
    }

    @GetMapping("/online/platform/{platform}")
    public Response getOnlineMobiles(@PathVariable Integer platform) {
        return mobileService.getOnlineMobiles(platform);
    }

}
