package com.daxiang.controller;

import com.daxiang.mbg.po.Category;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.CategoryService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/add")
    public Response add(@RequestBody @Valid Category category) {
        return categoryService.add(category);
    }

    @DeleteMapping("/{categoryId}/type/{type}/project/{projectId}")
    public Response delete(@PathVariable Integer categoryId, @PathVariable Integer type, @PathVariable Integer projectId) {
        return categoryService.delete(categoryId, type, projectId);
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({UpdateGroup.class}) Category category) {
        return categoryService.update(category);
    }

    @PostMapping("/list")
    public Response list(Category category, PageRequest pageRequest) {
        return categoryService.list(category, pageRequest);
    }

    @GetMapping("/tree")
    public Response getCategoryTree(Integer projectId, Integer type) {
        return categoryService.getCategoryTreeByProjectIdAndType(projectId, type);
    }

}
