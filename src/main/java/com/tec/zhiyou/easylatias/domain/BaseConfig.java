package com.tec.zhiyou.easylatias.domain;

/**
 * @author : hanhuafeng
 * @date : 2023/11/8 09:59
 */
public class BaseConfig {
    /**
     * 配置的模块名称
     */
    private String moduleName;

    /**
     * latias默认的请求路径
     */
    private String latiasDefaultZone;

    /**
     * latias client id
     */
    private String clientId;

    /**
     * latias client secret
     */
    private String clientSecret;

    /**
     * rayquaza root
     */
    private String rayquazaRoot;

    public BaseConfig() {
        this.moduleName = "";
        this.latiasDefaultZone = "";
        this.clientId = "";
        this.clientSecret = "";
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLatiasDefaultZone() {
        return latiasDefaultZone;
    }

    public void setLatiasDefaultZone(String latiasDefaultZone) {
        this.latiasDefaultZone = latiasDefaultZone;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRayquazaRoot() {
        return rayquazaRoot;
    }

    public void setRayquazaRoot(String rayquazaRoot) {
        this.rayquazaRoot = rayquazaRoot;
    }
}
