package com.tec.zhiyou.easylatias.util;

import com.tec.zhiyou.easylatias.domain.yaml.LatiasServiceProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * @author : hanhuafeng
 * @date : 2023/11/1 15:50
 */
public class YamlUtil implements Serializable {
    /**
     * 生成yaml文件
     *
     * @param yamlFilePath          yaml文件路径
     * @param latiasServiceProperty yaml文件内容
     * @throws IOException io异常
     */
    public static void generateYamlFile(String yamlFilePath, LatiasServiceProperty latiasServiceProperty) throws IOException {
        // 从 resources 目录下读取 yaml 文件
        try(InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream(yamlFilePath)) {
            // 读取模板内容

        }
    }
}
