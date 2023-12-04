package com.tec.zhiyou.easylatias.domain.enums;

/**
 * 0 正常接口 1 Rayquaza生成的接口
 *
 * @author : hanhuafeng
 * @date : 2023/12/2 17:34
 */
public enum TypeEnum {
    /**
     * 正常接口
     */
    NORMAL(0),
    /**
     * Rayquaza生成的接口
     */
    RAYQUAZA(1);

    private Integer type;

    TypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
