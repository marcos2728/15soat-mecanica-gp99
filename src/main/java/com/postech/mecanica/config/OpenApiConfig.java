package com.postech.mecanica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mecanicaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Mecânica")
                        .description("Sistema de gestão de clientes, veículos e ordens de serviço")
                        .version("v1"));
    }
}