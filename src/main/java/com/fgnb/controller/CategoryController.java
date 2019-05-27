package com.fgnb.controller;

import com.fgnb.mbg.po.Category;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.service.CategoryService;
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
    public Response list(Category category, PageRequest pageRequest) {
        return categoryService.list(category, pageRequest);
    }

}
