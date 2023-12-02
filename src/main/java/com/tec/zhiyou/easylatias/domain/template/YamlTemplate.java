package com.tec.zhiyou.easylatias.domain.template;

/**
 * @author : hanhuafeng
 * @date : 2023/11/3 14:58
 */
public class YamlTemplate {
    /**
     * latias 1.1.74 yaml模板
     */
    public static String LATIAS_YAML_TEMPLATE_1_1_74 = """
              latias:
                # 注册到latias；true：注册，false：不注册
                register-with-latias: ${REGISTER_WITH_LATIAS:true}
                interceptor-strategy: ${INTERCEPTOR_STRATEGY:allowed}
                service-url:
                  default-zone: ${SERVICE_URL:{{defaultZone}}}
                client:
                  id: ${CLIENT_ID:{{clientId}}}
                  secret: ${CLIENT_SECRET:{{clientSecret}}}
                relation-configuration:
                  # 角色权限关联关系
                  role:
              {{roleList}}
                permission:
              {{permissionList}}
                manually-report-resources:
              {{manuallyReportResources}}
                white-list:
                  - path: /**""";
}
