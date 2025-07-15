package com.example.smartixtest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API для работы с товарами")
                        .version("1.0")
                        .description("API для импорта товаров из внешнего сервиса и работы с ними")
                        .contact(new Contact()
                                .name("Smartix Test Task")
                                .email("example@example.com")));
    }
} 