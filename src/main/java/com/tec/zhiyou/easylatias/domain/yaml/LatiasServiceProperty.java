package com.tec.zhiyou.easylatias.domain.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tec.zhiyou.easylatias.domain.enums.InterceptorStrategyEnum;
import com.tec.zhiyou.easylatias.domain.enums.RegisterTypeEnum;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:19
 */
public class LatiasServiceProperty implements Serializable {
    @JsonProperty("register-with-latias")
    private boolean registerWithLatias = false;

    /**
     * 注册方式
     */
    private RegisterTypeEnum registerBy = RegisterTypeEnum.HTTP;

    /**
     * 拦截策略, 默认全部拦截
     */
    private InterceptorStrategyEnum interceptorStrategy = InterceptorStrategyEnum.forbidden;

    /**
     * 服务名称
     */
    private ServiceUrl serviceUrl;

    /**
     * 客户端配置
     */
    private Client client;

    /**
     * 关联关系上报配置
     */
    private RelationConfiguration relationConfiguration;

    /**
     * 手动上报资源
     */
    private Map<String, Set<BaseRoute>> manuallyReportResources = new LinkedHashMap<>();

    /**
     * 白名单
     */
    private Set<BaseRoute> whiteList = new LinkedHashSet<>();

    /**
     * 黑名单
     */
    private Set<BaseRoute> blackList = new LinkedHashSet<>();

    public boolean isRegisterWithLatias() {
        return registerWithLatias;
    }

    public void setRegisterWithLatias(boolean registerWithLatias) {
        this.registerWithLatias = registerWithLatias;
    }

    public RegisterTypeEnum getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(RegisterTypeEnum registerBy) {
        this.registerBy = registerBy;
    }

    public InterceptorStrategyEnum getInterceptorStrategy() {
        return interceptorStrategy;
    }

    public void setInterceptorStrategy(InterceptorStrategyEnum interceptorStrategy) {
        this.interceptorStrategy = interceptorStrategy;
    }

    public ServiceUrl getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(ServiceUrl serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RelationConfiguration getRelationConfiguration() {
        return relationConfiguration;
    }

    public void setRelationConfiguration(RelationConfiguration relationConfiguration) {
        this.relationConfiguration = relationConfiguration;
    }

    public Map<String, Set<BaseRoute>> getManuallyReportResources() {
        return manuallyReportResources;
    }

    public void setManuallyReportResources(Map<String, Set<BaseRoute>> manuallyReportResources) {
        this.manuallyReportResources = manuallyReportResources;
    }

    public Set<BaseRoute> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(Set<BaseRoute> whiteList) {
        this.whiteList = whiteList;
    }

    public Set<BaseRoute> getBlackList() {
        return blackList;
    }

    public void setBlackList(Set<BaseRoute> blackList) {
        this.blackList = blackList;
    }
}
