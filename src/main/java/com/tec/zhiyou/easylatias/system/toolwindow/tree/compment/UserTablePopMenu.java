package com.tec.zhiyou.easylatias.system.toolwindow.tree.compment;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.tec.zhiyou.easylatias.data.DataCenter;
import com.tec.zhiyou.easylatias.domain.UserInfo;
import com.tec.zhiyou.easylatias.system.toolwindow.LatiasToolWindow;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import com.tec.zhiyou.easylatias.util.UserPermissionUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * @author : hanhuafeng
 * @date : 2023/11/1 14:09
 */
public class UserTablePopMenu extends JPopupMenu {
    private final JTable userTable;
    private EditorTextField roleKey;
    private EditorTextField roleName;
    private CheckBoxTreeNode<String> treeNode;

    private final Project project;


    private final DefaultTableModel userTableModel;

    private final DefaultTreeModel roleTreeModel;

    public UserTablePopMenu(Project project,
                            JTable userTable,
                            CheckBoxTreeNode<String> treeNode,
                            DefaultTableModel userTableModel,
                            DefaultTreeModel roleTreeModel) {
        this.userTable = userTable;
        this.treeNode = treeNode;
        this.project = project;
        this.userTableModel = userTableModel;
        this.roleTreeModel = roleTreeModel;
        addItem();
    }

    /**
     * 添加菜单项
     */
    private void addItem() {

        JMenuItem addUserItem = new JMenuItem();
        addUserItem.setText("添加角色");
        addUserItem.addActionListener(e -> {
            // 打开一个窗体
            System.out.println("添加角色");
            DialogWrapper dialog = new DialogWrapper(true) {
                {
                    setTitle("添加角色");
                    init();
                }
                @Override
                protected @NotNull JComponent createCenterPanel() {
                    JPanel panel = new JPanel(new GridLayout(2, 2));
                    JLabel label = new JLabel("角色key");
                    roleKey = new EditorTextField("");
                    JLabel label1 = new JLabel("角色名称");
                    roleName = new EditorTextField("");
                    panel.add(label);
                    panel.add(roleKey);
                    panel.add(label1);
                    panel.add(roleName);
                    return panel;
                }

                @Override
                protected JComponent createSouthPanel() {
                    JPanel jPanel = new JPanel();
                    JButton okBtn = new JButton("确定");
                    okBtn.addActionListener(e -> {
                        // 判断是否有重复的key
                        for (int i = 0; i < userTableModel.getRowCount(); i++) {
                            if (userTableModel.getValueAt(i, 0).equals(roleKey.getText())) {
                                JOptionPane.showMessageDialog(null, "角色key重复", "提示", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        UserInfo userInfo = new UserInfo(roleKey.getText(), roleName.getText(), UserPermissionUtil.parsePermissionTreeToString(treeNode));
                        DataCenter.getInstance(project).getUserInfoList().add(userInfo);
                        userTableModel.addRow(new String[]{roleKey.getText(), roleName.getText()});
                        this.close(0);
                    });
                    JButton cancelBtn = new JButton("取消");
                    cancelBtn.addActionListener(e -> {
                        this.close(0);
                    });
                    jPanel.add(okBtn);
                    jPanel.add(cancelBtn);
                    return jPanel;
                }
            };
            dialog.show();
        });
        JMenuItem delUserItem = new JMenuItem();
        delUserItem.setText("删除角色");
        delUserItem.setEnabled(checkHasSelect());
        delUserItem.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            // 从数据中心删除
            userTableModel.removeRow(selectedRow);
        });
        this.add(addUserItem);
        this.add(delUserItem);
    }

    /**
     * 检查是否有选中的行
     *
     * @return true 有选中的行 false 没有选中的行
     */
    private boolean checkHasSelect() {
        return userTable.getSelectedRow() != -1;
    }

}
