package com.daxiang.controller;

import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.GlobalVarService;
import com.daxiang.validator.group.GlobalVarGroup;
import com.daxiang.validator.group.UpdateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@Slf4j
@Validated({GlobalVarGroup.class})
@RequestMapping("/globalVar")
public class GlobalVarController {

    @Autowired
    private GlobalVarService globalVarService;

    @PostMapping("/add")
    public Response add(@RequestBody @Validated({GlobalVarGroup.class}) GlobalVar globalVar) {
        return globalVarService.add(globalVar);
    }

    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody @NotEmpty(message = "全局变量不能为空") @Valid List<GlobalVar> globalVars) {
        return globalVarService.addBatch(globalVars);
    }

    @DeleteMapping("/{globalVarId}")
    public Response delete(@PathVariable Integer globalVarId) {
        return globalVarService.delete(globalVarId);
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({GlobalVarGroup.class, UpdateGroup.class}) GlobalVar globalVar) {
        return globalVarService.update(globalVar);
    }

    @PostMapping("/list")
    public Response list(GlobalVar globalVar, PageRequest pageRequest) {
        return globalVarService.list(globalVar, pageRequest);
    }

}
