package com.arpit.mythicgates.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI mythicGatesOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Mythic Gates API")
                        .version("1.0")
                        .description("REST API for the Mythic Gates RPG backend."))
                .components(new Components()

                        .addSecuritySchemes(
                                "Bearer Authentication",

                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")
                );
    }
}
