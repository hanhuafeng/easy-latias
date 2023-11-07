package com.tec.zhiyou.easylatias.domain.yaml;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:21
 */
public class ServiceUrl {

    /**
     * 默认域，latias服务地址，可写多个，可写在一行按“，”分隔
     */
    private String defaultZone;

    public String getDefaultZone() {
        return defaultZone;
    }

    public void setDefaultZone(String defaultZone) {
        this.defaultZone = defaultZone;
    }
}

