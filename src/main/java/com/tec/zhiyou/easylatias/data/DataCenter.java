package com.tec.zhiyou.easylatias.data;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.tec.zhiyou.easylatias.domain.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : hanhuafeng
 * @date : 2023/10/26 15:23
 */
@State(name = "DataCenter", storages = {@Storage("latias/latias-cache.xml")})
public class DataCenter implements PersistentStateComponent<DataCenter> {
    /**
     * 用户列表
     */
    private List<UserInfo> userInfoList;

    // 获取反序列化后的结果-项目级别
    public static DataCenter getInstance(Project project) {
        DataCenter dataCenter = project.getService(DataCenter.class);
        if (dataCenter.userInfoList == null) {
            dataCenter.userInfoList = new LinkedList<>();
        }
        return project.getService(DataCenter.class);
    }
    public DataCenter(){}
    public DataCenter(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @Override
    public @Nullable DataCenter getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DataCenter state) {
        System.out.println(state);
        XmlSerializerUtil.copyBean(state, this);
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @Override
    public void noStateLoaded() {
        PersistentStateComponent.super.noStateLoaded();
    }
}
