package com.tec.zhiyou.easylatias.system.toolwindow.tree;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.tec.zhiyou.easylatias.annotation.Annotation;
import com.tec.zhiyou.easylatias.util.PsiDocCommentUtil;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * 分类节点
 *
 * @author hanhuafeng
 */
public class CategoryTree {

    /**
     * 分类名称
     */
    private final String categoryName;
    /**
     * 元素数量
     */
    private final Integer elementCounts;

    /**
     * 图标
     */
    private final Icon icon;

    /**
     * psiClass
     */
    private final PsiClass psiClass;

    /**
     * 请求路径
     */
    private final String requestPath;

    /**
     * doc文档
     */
    private final String document;

    /**
     * hashCode
     */
    private String hashCode;

    public CategoryTree(String categoryName, Integer elementCounts, PsiClass psiClass) {
        this(categoryName, elementCounts, AllIcons.Nodes.Class, psiClass);
        // 计算 hashCode className+requestPath
        String className = psiClass.getName();
        String requestPath = this.requestPath;
        String format = String.format("rest:%s:%s", className, requestPath);
        // 转 md5
//        this.hashCode = Md5Crypt.md5Crypt(format.getBytes());
        this.hashCode = format;
    }

    public CategoryTree(String categoryName, Integer elementCounts, Icon icon, PsiClass psiClass) {
        this.categoryName = categoryName;
        this.elementCounts = elementCounts;
        this.icon = icon;
        this.psiClass = psiClass;

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
        this.requestPath = requestPath;
        // 取 ApiDoc 的值
        String document = "";
        PsiDocComment docComment = psiClass.getDocComment();
        if (ObjectUtils.isNotEmpty(docComment)) {
            StringBuilder doc = PsiDocCommentUtil.getDoc(docComment);
            document = doc.toString().strip();
        }
        this.document = document;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getElementCounts() {
        return elementCounts;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getDocument() {
        return document;
    }

    public String getHashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return String.format(
                "[%d]%s - %s",
                elementCounts,
                categoryName,
                document
        );
    }

}
