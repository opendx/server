package com.daxiang.controller;

import com.daxiang.mbg.po.Category;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.CategoryService;
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
    public Response add(@RequestBody @Valid Category category) {
        return categoryService.add(category);
    }

    @DeleteMapping("/{categoryId}")
    public Response delete(@PathVariable Integer categoryId) {
        return categoryService.delete(categoryId);
    }

    /**
     * 查询分类
     *
     * @param category
     * @return
     */
    @PostMapping("/list")
    public Response list(Category category, PageRequest pageRequest) {
        return categoryService.list(category, pageRequest);
    }

}
