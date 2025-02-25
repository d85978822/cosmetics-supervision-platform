package com.cosmetics.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("化妆品监管平台系统服务API")
                        .version("1.0")
                        .description("化妆品监管平台系统服务接口文档")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("support@example.com")));
    }
} 