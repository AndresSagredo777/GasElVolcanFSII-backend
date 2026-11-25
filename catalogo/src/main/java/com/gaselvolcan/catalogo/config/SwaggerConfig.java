package com.gaselvolcan.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SwaggerConfig {

    @Bean
    OpenAPI catalogoOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Gas El Volcan - API de Catálogo")
                .description("Consulta y administración de productos del catálogo.")
                .version("1.0.0"));
    }
}
