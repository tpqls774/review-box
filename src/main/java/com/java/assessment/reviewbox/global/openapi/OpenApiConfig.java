package com.java.assessment.reviewbox.global.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(info())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }

    private Info info() {
        return new Info().title("ReviewBox API").description("ReviewBox API").version("1.0.0");
    }
}
