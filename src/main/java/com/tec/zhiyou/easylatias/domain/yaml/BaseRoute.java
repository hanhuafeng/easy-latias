package com.tec.zhiyou.easylatias.domain.yaml;

import com.tec.zhiyou.easylatias.domain.enums.HttpMethod;

import java.util.Objects;


/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:25
 */
public class BaseRoute {

    private String path;

    private HttpMethod method;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseRoute baseRoute = (BaseRoute) o;
        return Objects.equals(path, baseRoute.path) && method == baseRoute.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}

