package com.tec.zhiyou.easylatias.system.toolwindow.tree;

import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 控制器节点
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 10:34
 */
public class RestElementNode extends CheckBoxTreeNode<CategoryTree> {

    public RestElementNode(@NotNull CategoryTree data) {
        super(data);
    }
}
