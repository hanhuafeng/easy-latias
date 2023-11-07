package com.tec.zhiyou.easylatias.system.toolwindow.listener;

import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.domain.UserInfo;
import com.tec.zhiyou.easylatias.system.toolwindow.LatiasToolWindow;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.CategoryTree;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.ControllerElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.RestElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import com.tec.zhiyou.easylatias.util.UserPermissionUtil;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * @author : hanhuafeng
 * @date : 2023/10/31 15:49
 */
public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
    private final JTree permissionTree;

    public CheckBoxTreeNodeSelectionListener(JTree permissionTree) {
        this.permissionTree = permissionTree;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        final int doubleClick = 2;
        if (event.getClickCount() > 0 && event.getClickCount() % doubleClick == 0) {
            return;
        }
        JTree tree = (JTree) event.getSource();
        int x = event.getX();
        int y = event.getY();

        // x必须要在26-50或者 44-58之间
        if (!(x > 26 && x < 50) && !(x > 44 && x < 58)) {
            return;
        }
        int row = tree.getRowForLocation(x, y);
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            CheckBoxTreeNode<?> node = (CheckBoxTreeNode<?>) path.getLastPathComponent();
            if (node != null) {
                boolean isSelected = !node.isSelected();
                node.setSelected(isSelected);
                ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
                // 设置用户的选择列表
                UserInfo seletctedUserInfo = LatiasToolWindow.SELECTED_USER_INFO;
                if (seletctedUserInfo != null) {
                    Object data = node.getData();
                    if (data instanceof CategoryTree categoryTree) {
                        seletctedUserInfo.getNodeHashCodeToSelectMap().put(categoryTree.getHashCode(), isSelected);
                        Enumeration<TreeNode> children = node.children();
                        while (children.hasMoreElements()) {
                            ControllerElementNode controllerElementNode = (ControllerElementNode) children.nextElement();
                            seletctedUserInfo.getNodeHashCodeToSelectMap().put(controllerElementNode.getData().getHashCode(), isSelected);
                        }
                    } else if (data instanceof RuleInfo ruleInfo) {
                        seletctedUserInfo.getNodeHashCodeToSelectMap().put(ruleInfo.getHashCode(), isSelected);
                    }
                }
            }
        }
        permissionTree.repaint();
    }
}
