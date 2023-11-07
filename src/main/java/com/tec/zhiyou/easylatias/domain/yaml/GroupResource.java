package com.tec.zhiyou.easylatias.domain.yaml;

import com.tec.zhiyou.easylatias.domain.enums.CrudEnum;

import java.util.Set;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:22
 */
public class GroupResource {

    private String name;

    private String rootPath;

    private Set<CrudEnum> crud;

    private Set<PermissionResource> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Set<CrudEnum> getCrud() {
        return crud;
    }

    public void setCrud(Set<CrudEnum> crud) {
        this.crud = crud;
    }

    public Set<PermissionResource> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionResource> permissions) {
        this.permissions = permissions;
    }
}
