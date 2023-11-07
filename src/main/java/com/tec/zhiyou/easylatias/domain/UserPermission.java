package com.tec.zhiyou.easylatias.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户和权限的关系
 *
 * @author : hanhuafeng
 * @date : 2023/11/3 23:37
 */
public class UserPermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nodeHashCode;

    private boolean selected;

    private List<UserPermission> permissionList;

    public UserPermission() {
    }

    public UserPermission(String nodeHashCode, boolean selected) {
        this.nodeHashCode = nodeHashCode;
        this.selected = selected;
        this.permissionList = new LinkedList<>();
    }

    public String getNodeHashCode() {
        return nodeHashCode;
    }

    public void setNodeHashCode(String nodeHashCode) {
        this.nodeHashCode = nodeHashCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setPermissionList(List<UserPermission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<UserPermission> getPermissionList() {
        return permissionList;
    }

}
