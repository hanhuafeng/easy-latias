package com.tec.zhiyou.easylatias.domain;

import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.tec.zhiyou.easylatias.annotation.Annotation;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.util.Objects;

/**
 * Rayquaza代码规则信息
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 09:43
 */
public class RayquazaRuleInfo {
    /**
     * 方法psi
     */
    private PsiElement methodPsiElement;

    /**
     * 方法上的注解
     */
    private Annotation annotation;

    /**
     * 方法上的注释
     */
    private String docComment;

    /**
     * 父类
     */
    private PsiClass psiClass;


    public PsiElement getMethodPsiElement() {
        return methodPsiElement;
    }

    public void setMethodPsiElement(PsiElement methodPsiElement) {
        this.methodPsiElement = methodPsiElement;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public String getDocComment() {
        return docComment;
    }

    public void setDocComment(String docComment) {
        this.docComment = docComment;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public String toString() {
        return "RuleInfo{" +
                "methodPsiElement=" + methodPsiElement +
                ", annotation=" + annotation +
                ", docComment='" + docComment + '\'' +
                '}';
    }

    /**
     * 取hashCode
     *
     * @return hashCode
     */
    public String getHashCode() {
        PsiMethod psiMethod = (PsiMethod) methodPsiElement;
        String requestPath = "";
        PsiAnnotation[] annotations = psiMethod.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if (ObjectUtils.isEmpty(annotation)) {
                continue;
            }
            String qualifiedName = annotation.getQualifiedName();
            PsiAnnotationMemberValue psiAnnotationMemberValue = annotation.findAttributeValue("value");
            if (Objects.equals(annotation.getQualifiedName(), qualifiedName) && ObjectUtils.isNotEmpty(psiAnnotationMemberValue)) {
                requestPath = psiAnnotationMemberValue.getText();
                break;
            }
        }
        requestPath = requestPath.replaceAll("\"", "");
//        return Md5Crypt.md5Crypt(String.format("%s.%s:%s:%s",
//                psiClass.getQualifiedName(),
//                psiMethod.getName(),
//                requestPath,
//                annotation.getHttpMethod().name()).getBytes());
        return String.format("%s.%s:%s:%s",
                psiClass.getQualifiedName(),
                psiMethod.getName(),
                requestPath,
                annotation.getHttpMethod().name());
    }

    public Icon getIcon() {
        if (annotation != null) {
            return annotation.getIcon();
        }
        return null;
    }

    /**
     * 导航
     *
     * @param focus 是否聚焦
     */
    public void navigate(boolean focus) {
        if (methodPsiElement != null) {
            ((Navigatable) methodPsiElement).navigate(focus);
        }
    }
}
