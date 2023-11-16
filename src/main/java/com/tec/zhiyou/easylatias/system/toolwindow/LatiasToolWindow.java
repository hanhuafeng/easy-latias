package com.tec.zhiyou.easylatias.system.toolwindow;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.tec.zhiyou.easylatias.annotation.Annotation;
import com.tec.zhiyou.easylatias.data.DataCenter;
import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.domain.UserInfo;
import com.tec.zhiyou.easylatias.domain.template.YamlTemplate;
import com.tec.zhiyou.easylatias.service.LatiasService;
import com.tec.zhiyou.easylatias.system.toolwindow.listener.CheckBoxTreeNodeSelectionListener;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.*;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.checkbox.CheckBoxTreeNode;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.compment.GlobalConfigDialog;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.compment.UserTablePopMenu;
import com.tec.zhiyou.easylatias.system.toolwindow.tree.render.CheckBoxTreeCellRenderer;
import com.tec.zhiyou.easylatias.util.AsyncUtils;
import com.tec.zhiyou.easylatias.util.PsiDocCommentUtil;
import com.tec.zhiyou.easylatias.util.UserPermissionUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : hanhuafeng
 * @date : 2023/10/26 14:09
 */
public class LatiasToolWindow {
    private JTextField modulePath;
    private JTable userTable;
    private JTree permissionTree;
    private JButton generateBtn;
    private JButton fresh;
    private JPanel easyLatiasToolWindow;
    private JScrollPane userTableScrollPane;
    private JButton configBtn;

    private final Project project;

    public static UserInfo SELECTED_USER_INFO;

    /**
     * 项目接口信息
     */
    private Map<PsiElement, List<RuleInfo>> elements;

    /**
     * 用户表头
     */
    public static final String[] USER_TABLE_HEADERS = {"key", "角色名称"};

    /**
     * 树根节点
     */
    private CheckBoxTreeNode<String> treeRoot;

    /**
     * 用户表格模型
     */
    private final DefaultTableModel userTableModel = new DefaultTableModel(null, USER_TABLE_HEADERS) {
        // 重写isCellEditable方法，禁止单元格编辑
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * 权限树
     */
    private final DefaultTreeModel roleTreeModel = new DefaultTreeModel(null);

    /**
     * 初始化
     */
    public void initData() {
        if (ObjectUtils.isNotEmpty(DataCenter.getInstance(project).getBaseConfig().getModuleName())) {
            modulePath.setText(DataCenter.getInstance(project).getBaseConfig().getModuleName());
        }
        userTable.setModel(userTableModel);
//        permissionTree.setCellRenderer(new PermissionTreeCellRenderer());
        permissionTree.setCellRenderer(new CheckBoxTreeCellRenderer());
        permissionTree.setRootVisible(true);
        permissionTree.setShowsRootHandles(false);
        DumbService.getInstance(project).runWhenSmart(() -> {
            AsyncUtils.runRead(project, this::getElements, map -> {
                renderTree(map);
                initListener();
            });
        });
    }

    /**
     * 获取所有RestController 以及下方所有的接口
     *
     * @return map key: RestController value: 接口信息
     */
    public Map<PsiElement, List<RuleInfo>> getElements() {
        return LatiasService.getInstance(project).findAllRestController();
    }

    public LatiasToolWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        permissionTree.setModel(roleTreeModel);
        initData();
    }

    private void initListener() {
        generateBtn.addActionListener(e -> {
            generateBtnListenEvent();
        });
        // 刷新按钮
        fresh.addActionListener(e -> {
            DumbService.getInstance(project).runWhenSmart(() -> {
                AsyncUtils.runRead(project, this::getElements, map -> {
                    this.elements = map;
                    renderTree(map);
                    // 设置选中的用户
                    if (SELECTED_USER_INFO != null) {
                        SELECTED_USER_INFO = DataCenter.getInstance(project).getUserInfoList().stream().filter(userInfo -> userInfo.getKey().equals(SELECTED_USER_INFO.getKey())).findFirst().orElse(null);
                        if (SELECTED_USER_INFO != null) {
                            UserPermissionUtil.parseStringToPermissionTree(SELECTED_USER_INFO.getNodeHashCodeToSelectMap(), treeRoot);
                            // 渲染树
                            roleTreeModel.setRoot(treeRoot);
                            roleTreeModel.reload();
                            permissionTree.repaint();
                        }
                    }
                });
            });
        });

        // 用户表格添加鼠标事件
        userTableScrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // 右键，则打开一个右键菜单
                    UserTablePopMenu userTablePopMenu = new UserTablePopMenu(project, userTable, generateTreeRoot(elements, false), userTableModel, roleTreeModel);
                    userTablePopMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });
        // 用户表格添加鼠标事件
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // 左键
                    userTableLeftClickListenEvent();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // 右键，则打开一个右键菜单
                    UserTablePopMenu userTablePopMenu = new UserTablePopMenu(project, userTable, treeRoot, userTableModel, roleTreeModel);
                    userTablePopMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

        modulePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chooseModule(project);
            }
        });

        // LiteFlowTree子项双击监听
        permissionTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int doubleClick = 2;
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 0 && e.getClickCount() % doubleClick == 0) {
                    RuleInfo node = getLastSelectedControllerElementNode(permissionTree);
                    if (node != null && e.getClickCount() == doubleClick) {
                        node.navigate(true);
                    }
                }
            }
        });

        // 选中监听
        permissionTree.addMouseListener(new CheckBoxTreeNodeSelectionListener(permissionTree));

        // 配置按钮
        configBtn.addActionListener(e -> {
            new GlobalConfigDialog(project).show();
        });
    }

    /**
     * 生成latias.yaml按钮监听事件
     */
    private void generateBtnListenEvent() {
        if (ObjectUtils.isEmpty(DataCenter.getInstance(project).getBaseConfig().getModuleName())) {
            JOptionPane.showMessageDialog(null, "请选择模块", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Map<String, String> yamlParamMap = new LinkedHashMap<>();
        List<String> permissionTempList = new LinkedList<>();
        List<String> manuallyReportResourcesTempList = new LinkedList<>();
        Map<String, List<String>> manuallyReportResourcesTempMap = new LinkedHashMap<>();
        List<String> roleList = getRoleList();
        getPermission(manuallyReportResourcesTempMap, permissionTempList);
        for (Map.Entry<String, List<String>> entry : manuallyReportResourcesTempMap.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            String manuallyReportResourcesTemplate = "      {{key}}: \n{{value}}";
            manuallyReportResourcesTemplate = manuallyReportResourcesTemplate.replace("{{key}}", key)
                    .replace("{{value}}", String.join("\n", value));
            manuallyReportResourcesTempList.add(manuallyReportResourcesTemplate);
        }
        yamlParamMap.put("roleList", String.join("\n", roleList));
        yamlParamMap.put("manuallyReportResources", String.join("\n", manuallyReportResourcesTempList));
        yamlParamMap.put("permissionList", String.join("\n", permissionTempList));

        // 渲染yaml
        String yaml = YamlTemplate.LATIAS_YAML_TEMPLATE_1_1_74
                .replace("{{roleList}}", yamlParamMap.get("roleList"))
                .replace("{{permissionList}}", yamlParamMap.get("permissionList"))
                .replace("{{manuallyReportResources}}", yamlParamMap.get("manuallyReportResources"))
                .replace("{{defaultZone}}", DataCenter.getInstance(project).getBaseConfig().getLatiasDefaultZone())
                .replace("{{clientId}}", DataCenter.getInstance(project).getBaseConfig().getClientId())
                .replace("{{clientSecret}}", DataCenter.getInstance(project).getBaseConfig().getClientSecret());
        try {
            FileWriter fileWriter = new FileWriter(DataCenter.getInstance(project).getBaseConfig().getModuleName() + "/src/main/resources/latias.yaml");
            fileWriter.write(yaml);
            fileWriter.close();
            JOptionPane.showMessageDialog(null, "生成成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "生成失败", "提示", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ioException);
        }
    }

    /**
     * 用户表格左键点击事件
     */
    private void userTableLeftClickListenEvent() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow > -1) {
            String key = (String) userTable.getValueAt(selectedRow, 0);
            SELECTED_USER_INFO = DataCenter.getInstance(project).getUserInfoList().stream().filter(userInfo -> userInfo.getKey().equals(key)).findFirst().orElse(null);
            if (SELECTED_USER_INFO != null) {
                UserPermissionUtil.parseStringToPermissionTree(SELECTED_USER_INFO.getNodeHashCodeToSelectMap(), treeRoot);
                // 渲染树
                roleTreeModel.setRoot(treeRoot);
                roleTreeModel.reload();
                permissionTree.repaint();
            }
        }
    }

    /**
     * 选择模块
     *
     * @param project 项目
     */
    private void chooseModule(Project project) {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        ChooseModulesDialog dialog = new ChooseModulesDialog(project, Arrays.asList(modules),
                "Choose Module",
                "Choose Single Module");
        dialog.setSingleSelectionMode();
        dialog.show();

        List<Module> chosenElements = dialog.getChosenElements();
        if (!chosenElements.isEmpty()) {
            Module module = chosenElements.get(0);
            chooseModulePath(module);
        }
    }

    /**
     * 选择模块路径
     *
     * @param module 模块
     */
    private void chooseModulePath(Module module) {

        String moduleDirPath = ModuleUtil.getModuleDirPath(module);
        int childModuleIndex = indexFromChildModule(moduleDirPath);
        if (hasChildModule(childModuleIndex)) {
            Optional<String> pathFromModule = getPathFromModule(module);
            if (pathFromModule.isPresent()) {
                moduleDirPath = pathFromModule.get();
            } else {
                moduleDirPath = moduleDirPath.substring(0, childModuleIndex);
            }
        }
        modulePath.setText(moduleDirPath);
        DataCenter.getInstance(project).getBaseConfig().setModuleName(moduleDirPath);
    }

    /**
     * 获取子模块索引
     *
     * @param moduleDirPath 模块路径
     * @return 子模块索引
     */
    private int indexFromChildModule(String moduleDirPath) {
        return moduleDirPath.indexOf(".idea");
    }

    /**
     * 是否有子模块
     *
     * @param childModuleIndex 子模块索引
     * @return 是否有子模块
     */
    private boolean hasChildModule(int childModuleIndex) {
        return childModuleIndex > -1;
    }

    /**
     * 获取模块路径
     *
     * @param module 模块
     * @return 路径
     */
    private Optional<String> getPathFromModule(Module module) {
        // 兼容gradle获取子模块
        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (contentRoots.length == 1) {
            return Optional.of(contentRoots[0].getPath());
        }
        return Optional.empty();
    }

    /**
     * 获取面板
     *
     * @return 面板
     */
    public JPanel getEasyLatiasToolWindow() {
        return easyLatiasToolWindow;
    }

    /**
     * 渲染树
     *
     * @param map map
     */
    public void renderTree(@NotNull Map<PsiElement, List<RuleInfo>> map) {
        this.elements = map;
        treeRoot = generateTreeRoot(map, true);
        if (ObjectUtils.isEmpty(DataCenter.getInstance(project).getUserInfoList())) {
            UserInfo userInfo = new UserInfo("super-admin", "超级管理员", UserPermissionUtil.parsePermissionTreeToString(treeRoot));
            DataCenter.getInstance(project).getUserInfoList().add(userInfo);
            userTableModel.addRow(new String[]{"super-admin", "超级管理员"});
            SELECTED_USER_INFO = userInfo;
        } else {
            List<UserInfo> userInfoList = DataCenter.getInstance(project).getUserInfoList();
            for (UserInfo userInfo : userInfoList) {
                userTableModel.addRow(new String[]{userInfo.getKey(), userInfo.getRoleName()});
            }
            SELECTED_USER_INFO = userInfoList.get(0);
            UserPermissionUtil.parseStringToPermissionTree(SELECTED_USER_INFO.getNodeHashCodeToSelectMap(), treeRoot);
        }
        // 面板展示用的树
        roleTreeModel.setRoot(treeRoot);
        roleTreeModel.reload();
        permissionTree.repaint();
        // 默认选中userTable的第一行
        userTable.setRowSelectionInterval(0, 0);
    }

    /**
     * 生成树
     *
     * @param map        map
     * @param changeTree 是否改变树
     * @return 树
     */
    private CheckBoxTreeNode<String> generateTreeRoot(Map<PsiElement, List<RuleInfo>> map, boolean changeTree) {
        AtomicInteger elementCount = new AtomicInteger();
        CheckBoxTreeNode<String> root = new CheckBoxTreeNode<>("Not found any controller");
        for (Map.Entry<PsiElement, List<RuleInfo>> entry : map.entrySet()) {
            PsiClass restClass = (PsiClass) entry.getKey();
            // 设置 class 节点
            RestElementNode restElementNode = new RestElementNode(new CategoryTree(restClass.getName(), entry.getValue().size(), restClass));
            entry.getValue().forEach(ruleInfo -> {
                // 添加 controller 节点
                restElementNode.add(new ControllerElementNode(ruleInfo));
                elementCount.incrementAndGet();
            });
            root.add(restElementNode);
        }
        if (changeTree) {
            permissionTree.firePropertyChange(JTree.ROOT_VISIBLE_PROPERTY, permissionTree.isRootVisible(), elementCount.get() < 1);
            permissionTree.setEnabled(elementCount.get() > 0);
        }
        return root;
    }

    /**
     * 获取RequestMapping的值
     *
     * @param psiClass psiClass
     * @return RequestMapping的值
     */
    private String getRequestMappingPath(PsiClass psiClass) {
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        // 取 RequestMapping 的值
        String requestPath = "";
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            if (Annotation.REQUESTING_MAPPING.getQualifiedName().equals(qualifiedName)) {
                requestPath = Objects.requireNonNull(annotation.findAttributeValue("value")).getText();
                break;
            }
        }
        requestPath = requestPath.replaceAll("\"", "");
        return requestPath;
    }

    /**
     * 取方法上的http 相关注释的路径
     *
     * @param psiMethod  方法
     * @param annotation 注解
     * @return 路径
     */
    private String getPsiMethodPath(PsiMethod psiMethod, Annotation annotation) {
        String path = "";
        PsiAnnotation[] annotations = psiMethod.getAnnotations();
        for (PsiAnnotation psiAnnotation : annotations) {
            if (Objects.equals(psiAnnotation.getQualifiedName(), annotation.getQualifiedName())) {
                path = Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText();
                break;
            }
        }
        return path.replaceAll("\"", "");
    }

    /**
     * 获取最后选中的节点
     *
     * @param tree 树
     * @return 节点
     */
    @Nullable
    private RuleInfo getLastSelectedControllerElementNode(@NotNull JTree tree) {
        DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (mutableTreeNode == null) {
            return null;
        }
        Object userObject = mutableTreeNode.getUserObject();
        if (userObject instanceof RuleInfo) {
            return (RuleInfo) userObject;
        }
        return null;
    }

    /**
     * 获取权限
     *
     * @param manuallyReportResourcesTempMap 上报的资源
     * @param permissionTempList             权限列表
     */
    private void getPermission(Map<String, List<String>> manuallyReportResourcesTempMap,
                               List<String> permissionTempList) {
        // 取权限
        for (Map.Entry<PsiElement, List<RuleInfo>> entry : elements.entrySet()) {
            String permissionTemplate = """
                            {{key}}:\s
                              name: {{name}}\s
                              permissions:\s
                    {{permissions}}""";
            PsiClass restClass = (PsiClass) entry.getKey();
            String key = restClass.getName();
            if (ObjectUtils.isEmpty(key)) {
                continue;
            }
            String restPath = getRequestMappingPath(restClass);
            if ("{}".equals(restPath)) {
                restPath = "";
            }
            if (ObjectUtils.isNotEmpty(restPath) && !restPath.endsWith("/")) {
                restPath = restPath + "/";
            }
            PsiDocComment docComment = restClass.getDocComment();
            String name = "";
            if (ObjectUtils.isNotEmpty(docComment)) {
                StringBuilder doc = PsiDocCommentUtil.getDoc(docComment);
                name = doc.toString().strip();
            }
            List<String> permissionsTempList = new LinkedList<>();
            List<RuleInfo> value = entry.getValue();
            for (RuleInfo ruleInfo : value) {
                String permissionsTemplate = "            - name: {{name}}\n              code: {{code}}";
                String manuallyReportResourcesTemplate = """
                                - path: {{restPath}}{{methodPath}}
                                  method: {{method}}\
                        """;
                String permission = ruleInfo.getDocComment();
                if (ObjectUtils.isEmpty(permission)) {
                    continue;
                }
                PsiMethod methodPsiElement = (PsiMethod) ruleInfo.getMethodPsiElement();
                String psiMethodPath = getPsiMethodPath(methodPsiElement, ruleInfo.getAnnotation());
                if ("{}".equals(psiMethodPath)) {
                    psiMethodPath = "";
                }
                if (ObjectUtils.isEmpty(psiMethodPath) && ObjectUtils.isNotEmpty(restPath) && restPath.endsWith("/")) {
                    // 移除最后一个/
                    restPath = restPath.substring(0, restPath.length() - 1);
                }
                if (restPath.endsWith("/") && psiMethodPath.startsWith("/")) {
                    psiMethodPath = psiMethodPath.substring(1);
                }
                permissionsTemplate = permissionsTemplate.replace("{{name}}", permission)
                        .replace("{{code}}", methodPsiElement.getName());
                // 渲染上报的资源文本
                //{{restName}}:{{methodName}}:\s
                String manuallyReportResourcesKey = key + ":" + methodPsiElement.getName();
                manuallyReportResourcesTemplate = manuallyReportResourcesTemplate.replace("{{restName}}", key)
                        .replace("{{methodName}}", methodPsiElement.getName())
                        .replace("{{restPath}}", restPath)
                        .replace("{{methodPath}}", psiMethodPath)
                        .replace("{{method}}", ruleInfo.getAnnotation().getHttpMethod().name());
                permissionsTempList.add(permissionsTemplate);
                manuallyReportResourcesTempMap.computeIfAbsent(manuallyReportResourcesKey, k -> new LinkedList<>()).add(manuallyReportResourcesTemplate);
            }
            permissionTemplate = permissionTemplate.replace("{{key}}", key)
                    .replace("{{name}}", name)
                    .replace("{{permissions}}", String.join("\n", permissionsTempList));
            permissionTempList.add(permissionTemplate);
        }
    }

    private List<String> getRoleList() {
        // 取用户
        List<UserInfo> userInfoList = DataCenter.getInstance(project).getUserInfoList();
        List<String> userRoleTempList = new LinkedList<>();
        for (UserInfo userInfo : userInfoList) {
            String roleTemplate = """
                            {{key}}:\s
                              name: {{roleName}}
                              permission:
                    {{permission}}\
                    """;
            String permission = "              all";
            UserPermissionUtil.parseStringToPermissionTree(userInfo.getNodeHashCodeToSelectMap(), treeRoot);
            Enumeration<TreeNode> children = treeRoot.children();
            // 如果第一层都选中了，则permission=all
            while (children.hasMoreElements()) {
                RestElementNode nextElement = (RestElementNode) children.nextElement();
                if (!nextElement.isSelected()) {
                    permission = "";
                }
            }

            if (ObjectUtils.isEmpty(permission)) {
                // 需要仔细遍历权限树了
                Set<String> permissionList = new LinkedHashSet<>();
                children = treeRoot.children();
                while (children.hasMoreElements()) {
                    RestElementNode restElement = (RestElementNode) children.nextElement();
                    // 子节点
                    Enumeration<TreeNode> controllerNode = restElement.children();
                    while (controllerNode.hasMoreElements()) {
                        ControllerElementNode controllerElementNode = (ControllerElementNode) controllerNode.nextElement();
                        if (controllerElementNode.isSelected()) {
                            String permissionTemplate = getPermissionString(restElement, controllerElementNode);
                            permissionList.add(permissionTemplate);
                        }
                    }
                }
                permission = String.join("\n", permissionList);
            }
            String key = userInfo.getKey();
            String roleName = userInfo.getRoleName();
            String role = roleTemplate.replace("{{key}}", key)
                    .replace("{{roleName}}", roleName)
                    .replace("{{permission}}", permission);
            userRoleTempList.add(role);
        }
        return userRoleTempList;
    }

    /**
     * 格式化角色权限的文本
     *
     * @param restElement           restElement
     * @param controllerElementNode controllerElementNode
     * @return 格式化后的文本
     */
    @NotNull
    private static String getPermissionString(RestElementNode restElement, ControllerElementNode controllerElementNode) {
        String className = restElement.getData().getCategoryName();
        PsiMethod methodPsiElement = (PsiMethod) controllerElementNode.getData().getMethodPsiElement();
        String methodName = methodPsiElement.getName();
        String permissionTemplate = "            - {{className}}:{{methodName}}";
        permissionTemplate = permissionTemplate.replace("{{className}}", className)
                .replace("{{methodName}}", methodName);
        return permissionTemplate;
    }
}
