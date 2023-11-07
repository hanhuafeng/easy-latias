package com.tec.zhiyou.easylatias.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * @author : hanhuafeng
 * @date : 2023/11/3 14:24
 */
public class PsiDocCommentUtil {

    @NotNull
    public static StringBuilder getDoc(PsiDocComment docComment) {
        StringBuilder doc = new StringBuilder();
        PsiElement[] descriptionElements = docComment.getDescriptionElements();
        for (PsiElement descriptionElement : descriptionElements) {
            if (descriptionElement instanceof PsiDocComment) {
                PsiElement[] descriptionElements1 = ((PsiDocComment) descriptionElement).getDescriptionElements();
                for (PsiElement element : descriptionElements1) {
                    doc.append(element.getText());
                }
            } else {
                doc.append(descriptionElement.getText());
            }
        }
        return doc;
    }
}
