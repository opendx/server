package com.daxiang.model.dto;

import com.daxiang.mbg.po.Category;
import com.daxiang.utils.Tree;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryTreeNode extends Category implements Tree.TreeNode {

    private List<Tree.TreeNode> children;

    public static CategoryTreeNode create(Category category) {
        CategoryTreeNode categoryTreeNode = new CategoryTreeNode();
        BeanUtils.copyProperties(category, categoryTreeNode);
        return categoryTreeNode;
    }
}
