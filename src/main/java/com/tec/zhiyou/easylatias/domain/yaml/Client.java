package com.tec.zhiyou.easylatias.domain.yaml;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:21
 */
public class Client {

    /**
     * app-id
     */
    private String id;

    /**
     * app-secret
     */
    private String secret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}

