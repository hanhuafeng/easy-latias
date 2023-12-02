package com.tec.zhiyou.easylatias.annotation;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.tec.zhiyou.easylatias.domain.enums.HttpMethod;
import com.tec.zhiyou.easylatias.icons.LatiasIcons;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.JavaPsiFacade;

import javax.swing.*;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hanhuafeng
 * @date : 2023/10/26 17:14
 */
public class Annotation implements Cloneable {
    public static final Annotation REST_CONTROLLER = new Annotation("@RestController",
            "org.springframework.web.bind.annotation.RestController",
            null,
            null);
    public static final Annotation REQUESTING_MAPPING = new Annotation("@RequestMapping",
            "org.springframework.web.bind.annotation.RequestMapping",
            null,
            null);
    public static final Annotation GET_MAPPING = new Annotation("@GetMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            LatiasIcons.GET_ICON,
            HttpMethod.GET);
    public static final Annotation DELETE_MAPPING = new Annotation("@DeleteMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            LatiasIcons.DELETE_ICON,
            HttpMethod.DELETE);
    public static final Annotation POST_MAPPING = new Annotation("@PostMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            LatiasIcons.POST_ICON,
            HttpMethod.POST);
    public static final Annotation PATCH_MAPPING = new Annotation("@PatchMapping",
            "org.springframework.web.bind.annotation.PatchMapping",
            LatiasIcons.PATCH_ICON,
            HttpMethod.PATCH);
    public static final Annotation PUT_MAPPING = new Annotation("@PutMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            LatiasIcons.PUT_ICON,
            HttpMethod.PUT);

    /**
     * RAYQUAZA的 Export 注解
     */
    public static final Annotation RAYQUAZA_EXPORT = new Annotation("@Export",
            "org.fatewa.engine.genius.annotations.Export",
            null,
            null);
    private final String label;

    private final String qualifiedName;

    private final Icon icon;

    private Map<String, AnnotationValue> attributePairs;

    private final HttpMethod httpMethod;

    /**
     * Instantiates a new Annotation.
     *
     * @param label         the label
     * @param qualifiedName the qualified name
     */
    public Annotation(@NotNull String label, @NotNull String qualifiedName, Icon icon, HttpMethod httpMethod) {
        this.label = label;
        this.qualifiedName = qualifiedName;
        attributePairs = Maps.newHashMap();
        this.icon = icon;
        this.httpMethod = httpMethod;
    }

    /**
     * The interface Annotation value.
     */
    public interface AnnotationValue {
    }

    private Annotation addAttribute(String key, AnnotationValue value) {
        this.attributePairs.put(key, value);
        return this;
    }

    @Override
    protected Annotation clone() throws CloneNotSupportedException {
        try {
            return (Annotation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(label);
        if (!Iterables.isEmpty(attributePairs.entrySet())) {
            builder.append(setupAttributeText());
        }
        return builder.toString();
    }

    private String setupAttributeText() {
        Optional<String> singleValue = getSingleValue();
        return singleValue.orElseGet(this::getComplexValue);
    }

    private Optional<String> getSingleValue() {
        try {
            String value = Iterables.getOnlyElement(attributePairs.keySet());
            String builder = "(" + attributePairs.get(value).toString() +
                    ")";
            return Optional.of(builder);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String getComplexValue() {
        StringBuilder builder = new StringBuilder("(");
        for (String key : attributePairs.keySet()) {
            builder.append(key);
            builder.append(" = ");
            builder.append(attributePairs.get(key).toString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }

    /**
     * To psi class optional.
     *
     * @param project the project
     * @return the optional
     */
    public Optional<PsiClass> toPsiClass(@NotNull Project project) {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(getQualifiedName(), GlobalSearchScope.allScope(project)));
    }

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    @NotNull
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * 获取图标
     *
     * @return 图标
     */
    public Icon getIcon() {
        return icon;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * The type String value.
     */
    public static class StringValue implements AnnotationValue {

        private final String value;

        /**
         * Instantiates a new String value.
         *
         * @param value the value
         */
        public StringValue(@NotNull String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "\"" + value + "\"";
        }

    }
}
