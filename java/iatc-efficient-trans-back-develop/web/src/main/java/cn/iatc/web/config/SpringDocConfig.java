package cn.iatc.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc API文档相关配置
 * Created by macro on 2022/3/4.
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI smartStationOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Smart Station API")
                        .description("SpringDoc API 演示")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("https://github.com/macrozheng/mall-learning")))
                .externalDocs(new ExternalDocumentation()
                        .description("智能站房文档")
                        .url("http://www.macrozheng.com"));
    }

    /**
     * 登陆模块
     * @return
     */
    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("login")
                .pathsToMatch("/login/**")
                .build();
    }

    /**
     * 用户模块
     * @return
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/user/**", "/users/**")
                .build();
    }

    /**
     * 角色模块
     * @return
     */
    @Bean
    public GroupedOpenApi roleApi() {
        return GroupedOpenApi.builder()
                .group("role")
                .pathsToMatch("/role/**")
                .build();
    }

    /**
     * 区域模块
     * @return
     */
    @Bean
    public GroupedOpenApi regionApi() {
        return GroupedOpenApi.builder()
                .group("region")
                .pathsToMatch("/region/**")
                .build();
    }

    /**
     * 站点模块
     * @return
     */
    @Bean
    public GroupedOpenApi stationApi() {
        return GroupedOpenApi.builder()
                .group("station")
                .pathsToMatch("/station/**", "/stations/**")
                .build();
    }

    /**
     * 类型模块
     * @return
     */
    @Bean
    public GroupedOpenApi typeApi() {
        return GroupedOpenApi.builder()
                .group("type")
                .pathsToMatch("/general_type/**")
                .build();
    }

    /**
     * 厂家模块
     * @return
     */
    @Bean
    public GroupedOpenApi factoryApi() {
        return GroupedOpenApi.builder()
                .group("factory")
                .pathsToMatch("/factory/**", "/factories/**")
                .build();
    }

    /**
     * 设备模块
     * @return
     */
    @Bean
    public GroupedOpenApi deviceApi() {
        return GroupedOpenApi.builder()
                .group("device")
                .pathsToMatch("/device/**", "/devices/**")
                .build();
    }

    /**
     * 设备类型模块
     * @return
     */
    @Bean
    public GroupedOpenApi deviceTypeApi() {
        return GroupedOpenApi.builder()
                .group("device_type")
                .pathsToMatch("/device_type/**")
                .build();
    }

    /**
     * 设备类型模块
     * @return
     */
    @Bean
    public GroupedOpenApi transformerInfoApi() {
        return GroupedOpenApi.builder()
                .group("transformer")
                .pathsToMatch("/transformer/**")
                .build();
    }


}
