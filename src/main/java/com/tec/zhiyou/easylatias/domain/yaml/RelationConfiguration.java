package com.tec.zhiyou.easylatias.domain.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:22
 */
public class RelationConfiguration {
    /**
     * 权限组
     */
    private Map<String, GroupResource> permission = new LinkedHashMap<>();

    /**
     * 角色
     */
    private Map<String, RoleResource> role = new LinkedHashMap<>();

    public Map<String, GroupResource> getPermission() {
        return permission;
    }

    public void setPermission(Map<String, GroupResource> permission) {
        this.permission = permission;
    }

    public Map<String, RoleResource> getRole() {
        return role;
    }

    public void setRole(Map<String, RoleResource> role) {
        this.role = role;
    }
}
