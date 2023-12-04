package com.tec.zhiyou.easylatias.system.toolwindow.tree.render;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.domain.enums.TypeEnum;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.*;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeLabel;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @author : hanhuafeng
 * @date : 2023/10/31 15:45
 */
public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer {
    protected JCheckBox check;
    protected CheckBoxTreeLabel label;

    public CheckBoxTreeCellRenderer() {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new CheckBoxTreeLabel());
//        check.setBackground(UIManager.getColor("Tree.textBackground"));
        check.setOpaque(false);
//        label.setForeground(UIManager.getColor("Tree.textForeground"));
    }

    /**
     * 返回的是一个<code>JPanel</code>对象，该对象中包含一个<code>JCheckBox</code>对象
     * 和一个<code>JLabel</code>对象。并且根据每个结点是否被选中来决定<code>JCheckBox</code>
     * 是否被选中。
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        check.setSelected(((CheckBoxTreeNode<?>) value).isSelected());
        label.setFont(tree.getFont());
        label.setText(stringValue);

        if (value instanceof RestElementNode) {
            RestElementNode node = (RestElementNode) value;
            CategoryTree data = node.getData();
            label.setIcon(data.getIcon());
            label.setText(data.toString());
        } else if (value instanceof ControllerElementNode) {
            ControllerElementNode node = (ControllerElementNode) value;
            RuleInfo data = node.getData();
            label.setIcon(data.getIcon());
            if (data.getTypeEnum() == TypeEnum.NORMAL) {
                PsiMethod methodPsiElement = (PsiMethod) data.getMethodPsiElement();
                String subName = data.getDocComment();
                label.setText(methodPsiElement.getName() + " - " + subName);
            } else if (data.getTypeEnum() == TypeEnum.RAYQUAZA) {
                label.setText(data.getAnnotation().getHttpMethod().name() + "-" + data.getRayquazaRuleInfoDetail().getPath());
            }
        } else {
            CheckBoxTreeNode<?> node = (CheckBoxTreeNode<?>) value;
            label.setText(node.toString());
        }
        label.setOpaque(false);
//        label.setBackground(Color.decode("#66B6FF"));
        label.setBackground(null);
        this.setOpaque(false);
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        return new Dimension(dCheck.width + dLabel.width, Math.max(dCheck.height, dLabel.height));
    }

    @Override
    public void doLayout() {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        int yCheck = 0;
        int yLabel = 0;
        if (dCheck.height < dLabel.height) {
            yCheck = (dLabel.height - dCheck.height) / 2;
        } else {
            yLabel = (dCheck.height - dLabel.height) / 2;
        }
        check.setLocation(0, yCheck);
        check.setBounds(0, yCheck, dCheck.width, dCheck.height);
        label.setLocation(dCheck.width, yLabel);
        label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
    }

    @Override
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }
}
