package com.tec.zhiyou.easylatias.domain.enums;

/**
 * @author : hanhuafeng
 * @date : 2023/11/2 14:22
 */
public enum CrudEnum {

    /**
     * 新增
     */
    CREATE("新增", "create"),

    /**
     * 查询
     */
    READ("查询", "read"),

    /**
     * 修改
     */
    UPDATE("修改", "update"),

    /**
     * 删除
     */
    DELETE("删除", "delete"),
    ;

    private final String name;

    private final String code;

    CrudEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
