package com.yqhp.controller;

import com.yqhp.mbg.po.Category;
import com.yqhp.model.Response;
import com.yqhp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    @PostMapping("/add")
    public Response addCategory(@RequestBody @Valid Category category) {
        return categoryService.addCategory(category);
    }

    /**
     * 查询分类
     *
     * @param category
     * @return
     */
    @PostMapping("/list")
    public Response list(Category category) {
        return categoryService.list(category);
    }


}
