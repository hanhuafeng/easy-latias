package com.tec.zhiyou.easylatias.system.toolwindow.tree.compment;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.tec.zhiyou.easylatias.data.DataCenter;
import com.tec.zhiyou.easylatias.domain.UserInfo;
import com.tec.zhiyou.easylatias.util.UserPermissionUtil;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author : hanhuafeng
 * @date : 2023/11/8 10:14
 */
public class GlobalConfigDialog extends DialogWrapper {
    private final Project project;

    private String defaultZone;

    private String clientId;

    private String clientSecret;
    /**
     * rayquaza生成接口的根路径
     */
    private String rayquazaRoot;

    private final EditorTextField defaultZoneTextField;
    private final EditorTextField clientIdTextField;
    private final EditorTextField clientSecretTextField;
    private final EditorTextField rayquazaRootTextField;

    public GlobalConfigDialog(Project project) {
        super(true);
        this.project = project;
        String defaultZone = DataCenter.getInstance(project).getBaseConfig().getLatiasDefaultZone();
        if (ObjectUtils.isEmpty(defaultZone)) {
            this.defaultZone = "";
        } else {
            this.defaultZone = defaultZone;
        }
        String clientId = DataCenter.getInstance(project).getBaseConfig().getClientId();
        if (ObjectUtils.isEmpty(clientId)) {
            clientId = "";
        }
        this.clientId = clientId;
        String clientSecret = DataCenter.getInstance(project).getBaseConfig().getClientSecret();
        if (ObjectUtils.isEmpty(clientSecret)) {
            clientSecret = "";
        }
        this.clientSecret = clientSecret;
        String rayquazaRoot = DataCenter.getInstance(project).getBaseConfig().getRayquazaRoot();
        if (ObjectUtils.isEmpty(rayquazaRoot)) {
            rayquazaRoot = "";
        }
        this.rayquazaRoot = rayquazaRoot;
        defaultZoneTextField = new EditorTextField(this.defaultZone);
        clientIdTextField = new EditorTextField(this.clientId);
        clientSecretTextField = new EditorTextField(this.clientSecret);
        rayquazaRootTextField = new EditorTextField(this.rayquazaRoot);
        defaultZoneTextField.setPreferredWidth(200);
        clientIdTextField.setPreferredWidth(200);
        clientSecretTextField.setPreferredWidth(200);
        rayquazaRootTextField.setPreferredWidth(200);
        init();
        setTitle("全局配置");
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("请求路径"));
        panel.add(defaultZoneTextField);
        panel.add(new JLabel("clientId"));
        panel.add(clientIdTextField);
        panel.add(new JLabel("clientSecret"));
        panel.add(clientSecretTextField);
        panel.add(new JLabel("rayquazaRoot"));
        panel.add(rayquazaRootTextField);
        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel jPanel = new JPanel();
        JButton okBtn = new JButton("确定");
        JButton cancelBtn = new JButton("取消");
        jPanel.add(okBtn);
        jPanel.add(cancelBtn);
        okBtn.addActionListener(e -> {
            this.defaultZone = defaultZoneTextField.getText();
            this.clientId = clientIdTextField.getText();
            this.clientSecret = clientSecretTextField.getText();
            this.rayquazaRoot = rayquazaRootTextField.getText();
            DataCenter.getInstance(project).getBaseConfig().setLatiasDefaultZone(this.defaultZone);
            DataCenter.getInstance(project).getBaseConfig().setClientId(this.clientId);
            DataCenter.getInstance(project).getBaseConfig().setClientSecret(this.clientSecret);
            DataCenter.getInstance(project).getBaseConfig().setRayquazaRoot(this.rayquazaRoot);
            this.close(0);
        });
        cancelBtn.addActionListener(e -> {
            this.close(0);
        });
        return jPanel;
    }
}
