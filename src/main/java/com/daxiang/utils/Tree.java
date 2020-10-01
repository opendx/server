package com.daxiang.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
public class Tree {

    public interface TreeNode {
        Integer getId();

        Integer getParentId();

        List<TreeNode> getChildren();

        void setChildren(List<TreeNode> children);
    }

    public static List<TreeNode> build(List<? extends TreeNode> nodes) {
        List<TreeNode> tree = new ArrayList<>();

        if (CollectionUtils.isEmpty(nodes)) {
            return tree;
        }

        Map<Integer, TreeNode> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity(), (k1, k2) -> k1));

        for (TreeNode node : nodes) {
            TreeNode parent = nodeMap.get(node.getParentId());
            if (parent == null) { // node为根节点
                tree.add(node);
            } else { // node为子节点
                List<TreeNode> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(node);
            }
        }

        return tree;
    }

}
