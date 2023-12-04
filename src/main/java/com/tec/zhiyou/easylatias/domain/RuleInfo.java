package com.tec.zhiyou.easylatias.domain;

import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.tec.zhiyou.easylatias.annotation.Annotation;
import com.tec.zhiyou.easylatias.domain.enums.TypeEnum;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.util.Objects;

/**
 * 代码规则信息
 *
 * @author : hanhuafeng
 * @date : 2023/10/31 09:43
 */
public class RuleInfo {
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

    /**
     * Rayquaza代码规则信息
     */
    private RayquazaRuleInfoDetail rayquazaRuleInfoDetail;

    /**
     * 类型
     */
    private TypeEnum typeEnum;

    public RuleInfo() {
    }

    public RuleInfo(PsiElement methodPsiElement, Annotation annotation, String docComment, PsiClass psiClass) {
        this.methodPsiElement = methodPsiElement;
        this.annotation = annotation;
        this.docComment = docComment;
        this.psiClass = psiClass;
    }

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
        String requestPath = "";
        String methodName;
        if (this.getTypeEnum() == TypeEnum.NORMAL) {
            PsiMethod psiMethod = (PsiMethod) methodPsiElement;
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
            methodName = psiMethod.getName();
            requestPath = requestPath.replaceAll("\"", "");
        } else {
            methodName = this.psiClass.getName();
            requestPath = this.rayquazaRuleInfoDetail.getPath();
        }
        return String.format("%s.%s:%s:%s",
                psiClass.getQualifiedName(),
                methodName,
                requestPath,
                annotation.getHttpMethod().name());
    }

    public Icon getIcon() {
        if (annotation != null) {
            return annotation.getIcon();
        }
        return null;
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public RayquazaRuleInfoDetail getRayquazaRuleInfoDetail() {
        return rayquazaRuleInfoDetail;
    }

    public void setRayquazaRuleInfoDetail(RayquazaRuleInfoDetail rayquazaRuleInfoDetail) {
        this.rayquazaRuleInfoDetail = rayquazaRuleInfoDetail;
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
