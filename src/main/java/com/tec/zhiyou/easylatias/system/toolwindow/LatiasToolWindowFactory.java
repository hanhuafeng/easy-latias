package com.tec.zhiyou.easylatias.system.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author : hanhuafeng
 * @date : 2023/10/26 14:37
 */
public class LatiasToolWindowFactory implements ToolWindowFactory {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        LatiasToolWindow latiasToolWindow = new LatiasToolWindow(project, toolWindow);
        toolWindow.getComponent().add(latiasToolWindow.getEasyLatiasToolWindow());
    }
}
