package com.example.de_tai_tt.fooddelivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI foodDeliveryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Food Delivery API")
                        .version("1.0.0")
                        .description("Spring Boot 4 backend API with JWT, PostgreSQL, JPA, MapStruct and OpenAPI"))
                .schemaRequirement("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
