package com.tec.zhiyou.easylatias.domain.enums;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:26
 */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private static final Map<String, HttpMethod> MAPPINGS = new HashMap<>(16);

    HttpMethod() {
    }

    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        return method != null ? MAPPINGS.get(method) : null;
    }

    public boolean matches(String method) {
        return this.name().equals(method);
    }

    static {
        HttpMethod[] var0 = values();
        int var1 = var0.length;

        for (HttpMethod httpMethod : var0) {
            MAPPINGS.put(httpMethod.name(), httpMethod);
        }

    }
}
