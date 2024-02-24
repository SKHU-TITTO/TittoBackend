package com.example.titto_backend.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Titto API Document")
                .version("v0.0.1")
                .description("Titto API 명세");

        String authName = "OAuth Token";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authName);
        Components components = new Components()
                .addSecuritySchemes(
                        authName,
                        new SecurityScheme()
                                .name(authName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                                .description("서버에서 받은 엑세스 토큰을 입력해주세요.")
                );

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(info);
    }

}
