package com.daxiang.controller;

import com.daxiang.mbg.po.Category;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.vo.CategoryVo;
import com.daxiang.service.CategoryService;
import com.daxiang.utils.Tree;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        categoryService.add(category);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{categoryId}/type/{type}/project/{projectId}")
    public Response delete(@PathVariable Integer categoryId, @PathVariable Integer type, @PathVariable Integer projectId) {
        categoryService.delete(categoryId, type, projectId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({UpdateGroup.class}) Category category) {
        categoryService.update(category);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Category query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<CategoryVo> pagedData = categoryService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<CategoryVo> categoryVos = categoryService.getCategoryVos(query, orderBy);
            return Response.success(categoryVos);
        }
    }

    @GetMapping("/tree")
    public Response getCategoryTree(Integer projectId, Integer type) {
        List<Tree.TreeNode> tree = categoryService.getCategoryTreeByProjectIdAndType(projectId, type);
        return Response.success(tree);
    }

}