package com.tec.zhiyou.easylatias.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.compiled.ClsReferenceExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class JavaService implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Project project;

    private final JavaPsiFacade javaPsiFacade;

    public JavaService(Project project) {
        this.project = project;
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    }

    public static JavaService getInstance(@NotNull Project project) {
        return project.getService(JavaService.class);
    }

    /**
     * 获取一个全名为qualifiedName的Class
     *
     * @param qualifiedName 类的全名
     * @return 返回一个PsiClass对象
     */
    public PsiClass getClassByQualifiedName(@NotNull String qualifiedName) {
        return javaPsiFacade.findClass(qualifiedName, GlobalSearchScope.allScope(this.project));
    }

    /**
     * 获取含有指定Annotation的所有Class
     *
     * @param qualifiedName 注解的全名
     * @return 含有指定qualifiedName的注解的所有Class
     */
    public Collection<PsiClass> getClassesByAnnotationQualifiedName(@NotNull String qualifiedName) {
        PsiClass psiClass = getClassByQualifiedName(qualifiedName);
        if (psiClass == null) {
            return new ArrayList<>();
        }
        return AnnotatedElementsSearch.searchPsiClasses(psiClass, GlobalSearchScope.projectScope(this.project)).findAll();
    }

    /**
     * 获取含有指定Annotation的所有Method
     *
     * @param qualifiedName 注解的全名
     * @return 含有指定qualifiedName的注解的所有Method
     */
    public Collection<PsiMethod> getMethodsByAnnotationQualifiedName(@NotNull String qualifiedName) {
        PsiClass psiClass = getClassByQualifiedName(qualifiedName);
        if (psiClass == null) {
            return new ArrayList<>();
        }
        return AnnotatedElementsSearch.searchPsiMethods(psiClass, GlobalSearchScope.projectScope(this.project)).findAll();
    }

    /**
     * 获取Class的指定注解属性值
     *
     * @param psiModifierListOwner Class类对象
     * @param qualifiedName        注解的全名
     * @param attribute            属性名
     * @return 属性值
     */
    public String getAnnotationAttributeValue(@NotNull PsiModifierListOwner psiModifierListOwner, @NotNull String qualifiedName, String attribute) {
        PsiAnnotation annotation = psiModifierListOwner.getAnnotation(qualifiedName);
        if (annotation == null) {
            return null;
        }
        PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue(attribute);
        if (attributeValue == null) {
            return null;
        }
        /*处理注解value是引用表达式*/
        if (attributeValue instanceof PsiReferenceExpressionImpl || attributeValue instanceof ClsReferenceExpressionImpl) {
            PsiElement resolve = ((PsiReferenceExpression) attributeValue).resolve();
            if (resolve == null) {
                return null;
            }
            String[] split = resolve.getText().split("=");
            int length = split.length;
            if (length == 2) {
                return split[1].trim().replace("\"", "").replace(";", "");
            } else if (length == 1) {
                return split[0].trim().replace("\"", "").replace(";", "");
            }
        }


        return attributeValue.getText().replace("\"", "");
    }
}
