package com.tec.zhiyou.easylatias.domain;

import com.tec.zhiyou.easylatias.system.toolwindow.tree.RestElementNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;

import javax.swing.tree.DefaultTreeModel;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : hanhuafeng
 * @date : 2023/10/26 15:33
 */
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色 key
     */
    private String key;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色绑定一棵权限树
     */
    private Map<String, Boolean> nodeHashCodeToSelectMap;

    public UserInfo() {
        nodeHashCodeToSelectMap = new LinkedHashMap<>();
    }

    public UserInfo(String key, String roleName, List<UserPermission> permissionTree) {
        this.key = key;
        this.roleName = roleName;
        this.nodeHashCodeToSelectMap = new LinkedHashMap<>();
        for (UserPermission userPermission : permissionTree) {
            // 第一层节点是根节点
            if (userPermission.isSelected()) {
                nodeHashCodeToSelectMap.put(userPermission.getNodeHashCode(), userPermission.isSelected());
            }
            if (userPermission.getPermissionList() != null) {
                for (UserPermission permission : userPermission.getPermissionList()) {
                    if (permission.isSelected()) {
                        nodeHashCodeToSelectMap.put(permission.getNodeHashCode(), permission.isSelected());
                    }
                }
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Map<String, Boolean> getNodeHashCodeToSelectMap() {
        return nodeHashCodeToSelectMap;
    }

    public void setNodeHashCodeToSelectMap(Map<String, Boolean> nodeHashCodeToSelectMap) {
        this.nodeHashCodeToSelectMap = nodeHashCodeToSelectMap;
    }
}
