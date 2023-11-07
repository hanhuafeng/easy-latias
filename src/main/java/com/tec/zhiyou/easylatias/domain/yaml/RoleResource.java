package com.tec.zhiyou.easylatias.domain.yaml;

import java.util.Set;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:24
 */
public class RoleResource {
    private String name;

    private Set<String> permission;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPermission() {
        return permission;
    }

    public void setPermission(Set<String> permission) {
        this.permission = permission;
    }
}

