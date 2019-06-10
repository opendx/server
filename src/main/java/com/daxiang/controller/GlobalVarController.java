package com.daxiang.controller;

import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.GlobalVarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@Slf4j
@RequestMapping("/globalVar")
public class GlobalVarController {

    @Autowired
    private GlobalVarService globalVarService;

    /**
     * 添加全局变量
     *
     * @param globalVar
     * @return
     */
    @PostMapping("/add")
    public Response add(@RequestBody @Valid GlobalVar globalVar) {
        return globalVarService.add(globalVar);
    }

    /**
     * 删除全局变量
     *
     * @param globalVarId
     * @return
     */
    @DeleteMapping("/{globalVarId}")
    public Response delete(@PathVariable Integer globalVarId) {
        return globalVarService.delete(globalVarId);
    }

    /**
     * 修改全局变量
     *
     * @param globalVar
     * @return
     */
    @PostMapping("/update")
    public Response update(@RequestBody @Valid GlobalVar globalVar) {
        return globalVarService.update(globalVar);
    }

    /**
     * 查询全局变量列表
     *
     * @param globalVar
     * @return
     */
    @PostMapping("/list")
    public Response list(GlobalVar globalVar, PageRequest pageRequest) {
        return globalVarService.list(globalVar, pageRequest);
    }

}
