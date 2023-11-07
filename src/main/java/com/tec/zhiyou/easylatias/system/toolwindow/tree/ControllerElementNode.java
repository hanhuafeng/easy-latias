package com.tec.zhiyou.easylatias.system.toolwindow.tree;

import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 接口节点
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 10:34
 */
public class ControllerElementNode extends CheckBoxTreeNode<RuleInfo> {

    public ControllerElementNode(@NotNull RuleInfo data) {
        super(data);
    }
}
