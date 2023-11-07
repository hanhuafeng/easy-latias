package com.tec.zhiyou.easylatias.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.tec.zhiyou.easylatias.annotation.Annotation;
import com.tec.zhiyou.easylatias.domain.RuleInfo;
import com.tec.zhiyou.easylatias.util.PsiDocCommentUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * @author : hanhuafeng
 * @date : 2023/10/30 13:44
 */
public class LatiasService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Project project;

    private final JavaService javaService;

    public LatiasService(Project project) {
        this.project = project;
        this.javaService = JavaService.getInstance(project);
    }

    public static LatiasService getInstance(@NotNull Project project) {
        return project.getService(LatiasService.class);
    }

    /**
     * 寻找所有的LiteFlowChain
     *
     * @return 返回所有Chain的XmlTag
     */
    public PsiElement[] findAllLiteFlowChain() {
        return null;
    }

    /**
     * 寻找所有的RestController涉及到的PsiClass 以及下属的所有方法，同时带上方法对应的注解
     *
     * @return 返回所有PsiClass
     */

    public Map<PsiElement, List<RuleInfo>> findAllRestController() {
        Map<PsiElement, List<RuleInfo>> result = new LinkedHashMap<>();
        Collection<PsiClass> restControllerComponents = javaService.getClassesByAnnotationQualifiedName(Annotation.REST_CONTROLLER.getQualifiedName());
        PsiElement[] temp = restControllerComponents.stream().filter(item -> isController(item, Annotation.REST_CONTROLLER)).distinct().toArray(PsiElement[]::new);
        for (PsiElement psiElement : temp) {
            List<RuleInfo> psiElements = new LinkedList<>();
            if (psiElement instanceof PsiClass psiClass) {
                for (PsiMethod method : psiClass.getMethods()) {
                    if (isController(method)) {
                        RuleInfo ruleInfo = new RuleInfo();
                        ruleInfo.setMethodPsiElement(method);
                        ruleInfo.setPsiClass(psiClass);
                        // 取方法 doc
                        PsiDocComment docComment = method.getDocComment();
                        if (docComment != null) {
                            StringBuilder doc = PsiDocCommentUtil.getDoc(docComment);
                            if (ObjectUtils.isNotEmpty(doc.toString())) {
                                String s = doc.toString().strip().replaceAll("\n", "");
                                System.out.println(method.getName() + ":" + s);
                                ruleInfo.setDocComment(s);
                            }
                        }
                        PsiAnnotation[] annotations = method.getAnnotations();
                        if (!ArrayUtils.isEmpty(annotations)) {
                            for (PsiAnnotation psiAnnotation : annotations) {
                                if (Objects.equals(psiAnnotation.getQualifiedName(), Annotation.DELETE_MAPPING.getQualifiedName())) {
                                    ruleInfo.setAnnotation(Annotation.DELETE_MAPPING);
                                } else if (Objects.equals(psiAnnotation.getQualifiedName(), Annotation.GET_MAPPING.getQualifiedName())) {
                                    ruleInfo.setAnnotation(Annotation.GET_MAPPING);
                                } else if (Objects.equals(psiAnnotation.getQualifiedName(), Annotation.PATCH_MAPPING.getQualifiedName())) {
                                    ruleInfo.setAnnotation(Annotation.PATCH_MAPPING);
                                } else if (Objects.equals(psiAnnotation.getQualifiedName(), Annotation.POST_MAPPING.getQualifiedName())) {
                                    ruleInfo.setAnnotation(Annotation.POST_MAPPING);
                                } else if (Objects.equals(psiAnnotation.getQualifiedName(), Annotation.PUT_MAPPING.getQualifiedName())) {
                                    ruleInfo.setAnnotation(Annotation.PUT_MAPPING);
                                }
                            }
                        }
                        psiElements.add(ruleInfo);
                    }
                }
            }
            result.put(psiElement, psiElements);
        }

        return result;
    }

    /**
     * 根据XmlTag获取LiteFlowComponent的名称
     *
     * @param xmlTag xmlTag
     * @return 返回LiteFlowComponent的名称
     */
    public String getLiteFlowComponentNameByXmlTag(@NotNull XmlTag xmlTag) {

        return null;
    }

    /**
     * 根据Method获取LiteFlowComponent的名称
     *
     * @param psiMethod psi方法
     * @return 返回LiteFlowComponent的名称
     */
    public String getLiteFlowComponentNameByPsiMethod(@NotNull PsiMethod psiMethod) {
        if (!this.isController(psiMethod)) {
            return null;
        }
        String componentValue = javaService.getAnnotationAttributeValue(psiMethod, Annotation.GET_MAPPING.getQualifiedName(), "nodeId");
        if (StringUtil.isEmpty(componentValue)) {
            return null;
        }
        return componentValue;
    }

    /**
     * 根据Class获取LiteFlowComponent的名称
     *
     * @param psiClass psi类
     * @return 返回LiteFlowComponent的名称
     */
    public String getLiteFlowComponentNameByPsiClass(@NotNull PsiClass psiClass) {


        return null;
    }

    private boolean isController(PsiElement psiElement, Annotation annotation) {
        if (psiElement instanceof PsiClass psiClass) {
            PsiClass nodeClazz = JavaService.getInstance(project).getClassByQualifiedName(annotation.getQualifiedName());
            // 排除所有包名以 org.fatewa.engine.genius.base. 开头的Class
            if (Objects.requireNonNull(psiClass.getQualifiedName()).indexOf("org.fatewa.engine.genius.base.") == 0) {
                return false;
            }
            // && psiClass.isInheritor(nodeClazz, true)判断是否有 extends 关系
            return nodeClazz != null;
        } else if (psiElement instanceof PsiMethod psiMethod) {
            PsiAnnotation[] annotations = psiMethod.getAnnotations();
            if (ArrayUtils.isEmpty(annotations)) {
                return false;
            }
            for (PsiAnnotation psiAnnotation : annotations) {
                if (Objects.equals(psiAnnotation.getQualifiedName(), annotation.getQualifiedName())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean isRestController(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.REST_CONTROLLER);
    }

    public boolean isPostMapping(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.POST_MAPPING);
    }

    public boolean isDeleteMapping(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.DELETE_MAPPING);
    }

    public boolean isPutMapping(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.PUT_MAPPING);
    }

    public boolean isPatchMapping(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.PATCH_MAPPING);
    }

    public boolean isGetMapping(@NotNull PsiElement psiElement) {
        return isController(psiElement, Annotation.GET_MAPPING);
    }

    public boolean isLiteFlowMultiComponent(@NotNull PsiClass psiClass) {
        if (psiClass.hasAnnotation(Annotation.REST_CONTROLLER.getQualifiedName())) {
            return false;
        }
        for (PsiMethod method : psiClass.getMethods()) {
            if (isController(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是LiteFlowComponent
     *
     * @param psiElement psi元素
     * @return 返回true或者false
     */
    public boolean isController(@NotNull PsiElement psiElement) {
        return (
                isGetMapping(psiElement) ||
                        isPatchMapping(psiElement) ||
                        isPutMapping(psiElement) ||
                        isDeleteMapping(psiElement) ||
                        isGetMapping(psiElement) ||
                        isPostMapping(psiElement)
        );
    }
}
