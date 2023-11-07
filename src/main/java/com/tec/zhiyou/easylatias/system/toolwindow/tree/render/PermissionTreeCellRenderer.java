package com.tec.zhiyou.easylatias.system.toolwindow.tree.render;

import com.intellij.psi.PsiMethod;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.CategoryTree;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.ControllerElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.RestElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.TreeNode;
import org.jetbrains.annotations.NotNull;


import javax.swing.*;

/**
 * 权限树渲染器
 *
 * @author hanhuafeng
 */
public class PermissionTreeCellRenderer extends ColoredTreeCellRenderer {


    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof RestElementNode) {
            RestElementNode node = (RestElementNode) value;
            CategoryTree data = node.getData();
            setIcon(data.getIcon());
            append(data.toString());
        } else if (value instanceof ControllerElementNode) {
            ControllerElementNode node = (ControllerElementNode) value;
            RuleInfo data = node.getData();
            setIcon(data.getIcon());
            PsiMethod methodPsiElement = (PsiMethod) data.getMethodPsiElement();
            append(methodPsiElement.getName());
//            String subName = data.getAnnotation().getQualifiedName();
            String subName = data.getDocComment();
            append(" - " + subName, SimpleTextAttributes.GRAYED_ATTRIBUTES);
        } else if (value instanceof TreeNode<?>) {
            TreeNode<?> node = (TreeNode<?>) value;
            append(node.toString());
        }
    }


}
