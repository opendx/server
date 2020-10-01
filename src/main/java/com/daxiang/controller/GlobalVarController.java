package com.daxiang.controller;

import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.GlobalVarVo;
import com.daxiang.service.GlobalVarService;
import com.daxiang.validator.group.GlobalVarGroup;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Validated({GlobalVarGroup.class})
@RestController
@RequestMapping("/globalVar")
public class GlobalVarController {

    @Autowired
    private GlobalVarService globalVarService;

    @PostMapping("/add")
    public Response add(@RequestBody @Validated({GlobalVarGroup.class}) GlobalVar globalVar) {
        globalVarService.add(globalVar);
        return Response.success("添加成功");
    }

    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody @NotEmpty(message = "全局变量不能为空") @Valid List<GlobalVar> globalVars) {
        globalVarService.addBatch(globalVars);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{globalVarId}")
    public Response delete(@PathVariable Integer globalVarId) {
        globalVarService.delete(globalVarId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({GlobalVarGroup.class, UpdateGroup.class}) GlobalVar globalVar) {
        globalVarService.update(globalVar);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(GlobalVar query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<GlobalVarVo> pagedData = globalVarService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<GlobalVarVo> globalVarVos = globalVarService.getGlobalVarVos(query, orderBy);
            return Response.success(globalVarVos);
        }
    }

}
