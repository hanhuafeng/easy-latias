package com.tec.zhiyou.easylatias.util;

import com.tec.zhiyou.easylatias.domain.UserPermission;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.ControllerElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.RestElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * 用户权限树的解析和反解析工具类
 *
 * @author : hanhuafeng
 * @date : 2023/11/3 23:34
 */
public class UserPermissionUtil {
    /**
     * 将权限树解析成能序列化的权限列表
     *
     * @param tree 权限树
     * @return 权限列表
     */
    public static List<UserPermission> parsePermissionTreeToString(CheckBoxTreeNode<String> tree) {
        List<UserPermission> result = new LinkedList<>();
        Enumeration<TreeNode> children = tree.children();
        while (children.hasMoreElements()) {
            RestElementNode nextElement = (RestElementNode) children.nextElement();
            UserPermission userPermission = new UserPermission(nextElement.getData().getHashCode(), nextElement.isSelected());
            result.add(userPermission);
            Enumeration<TreeNode> controllerNode = nextElement.children();
            while (controllerNode.hasMoreElements()) {
                ControllerElementNode controllerElementNode = (ControllerElementNode) controllerNode.nextElement();
                userPermission.getPermissionList().add(new UserPermission(controllerElementNode.getData().getHashCode(), controllerElementNode.isSelected()));
            }
        }
        return result;
    }

    /**
     * 将字符串解析成权限树
     *
     * @param hashCodeToSelectedMap hashCode -> 选中状态
     */
    public static void parseStringToPermissionTree(Map<String, Boolean> hashCodeToSelectedMap, CheckBoxTreeNode<String> root) {
        // 遍历树，将节点的选中状态设置为 true
        setNodeSelect(root, hashCodeToSelectedMap);
    }

    private static void setNodeSelect(CheckBoxTreeNode<String> root, Map<String, Boolean> hashCodeToSelectMap) {
        if (ObjectUtils.isEmpty(hashCodeToSelectMap)) {
            hashCodeToSelectMap = new HashMap<>(0);
        }
        // 设置选中状态
        Enumeration<TreeNode> newTreeChildren = root.children();
        while (newTreeChildren.hasMoreElements()) {
            RestElementNode nextElement = (RestElementNode) newTreeChildren.nextElement();
            String hashCode = nextElement.getData().getHashCode();
            Boolean checked = hashCodeToSelectMap.get(hashCode);
            nextElement.setSelected(Objects.requireNonNullElse(checked, false));
            // 判断子类的选中状态
            Enumeration<TreeNode> controllerNode = nextElement.children();
            while (controllerNode.hasMoreElements()) {
                ControllerElementNode controllerElementNode = (ControllerElementNode) controllerNode.nextElement();
                String controllerHashCode = controllerElementNode.getData().getHashCode();
                Boolean controllerChecked = hashCodeToSelectMap.get(controllerHashCode);
                controllerElementNode.setSelected(Objects.requireNonNullElse(controllerChecked, false));
            }
        }
    }
}
