package com.tec.zhiyou.easylatias.domain;

import com.tec.zhiyou.easylatias.annotation.Annotation;
import com.tec.zhiyou.easylatias.domain.enums.HttpMethod;

/**
 * Rayquaza代码规则信息
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 09:43
 */
public class RayquazaRuleInfoDetail {
    /**
     * 方法的请求类型
     */
    private HttpMethod httpMethod;

    /**
     * 路径
     */
    private String path;

    public RayquazaRuleInfoDetail() {
    }

    public RayquazaRuleInfoDetail(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
