package com.vitorbetmann.cleanresmapi.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cleanResmapiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("clean-resmapi")
                        .description("Restaurant management system backend — Tech Challenge Phase 2 (FIAP Pós Tech, Arquitetura e Desenvolvimento Java). Covers user type management, restaurant registration, and menu item registration, built with Clean Architecture.")
                        .version("0.0.1-SNAPSHOT"));
    }
}
