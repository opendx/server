package com.daxiang.controller;

import com.daxiang.mbg.po.Mobile;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.MobileVo;
import com.daxiang.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        mobileService.save(mobile);
        return Response.success("保存成功");
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{mobileId}")
    public Response delete(@PathVariable String mobileId) {
        mobileService.deleteAndClearRelatedRes(mobileId);
        return Response.success("删除成功");
    }

    @PostMapping("/list")
    public Response list(Mobile query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<MobileVo> pagedData = mobileService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<MobileVo> mobileVos = mobileService.getMobileVos(query, orderBy);
            return Response.success(mobileVos);
        }
    }

    @GetMapping("/{mobileId}/start")
    public Response start(@PathVariable String mobileId) {
        Mobile mobile = mobileService.start(mobileId);
        return Response.success(mobile);
    }

    @GetMapping("/online/platform/{platform}")
    public Response getOnlineMobiles(@PathVariable Integer platform) {
        List<Mobile> onlineMobiles = mobileService.getOnlineMobiles(platform);
        return Response.success(onlineMobiles);
    }

}
