package com.tec.zhiyou.easylatias.system.toolwindow.tree;

import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 树节点
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 10:26
 */
public class TreeNode<T> extends DefaultMutableTreeNode {

    private final T data;

    public TreeNode(@NotNull T data) {
        super(data);
        this.data = data;
    }

    @NotNull
    public T getData() {
        return data;
    }
}
