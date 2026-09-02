/*
 * This class configures the OpenAPI/Swagger documentation for the
 * DreamCart API. It adds a title, description, and a JWT "Authorize"
 * button in Swagger UI so protected endpoints can be tested directly
 * from the docs (paste in a token from POST /api/auth/login).
 */
package com.dreamcart.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI dreamCartOpenAPI() {

        SecurityScheme bearerScheme = new SecurityScheme()
                .name(BEARER_AUTH_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("DreamCart API")
                        .description("REST API for the DreamCart e-commerce platform - "
                                + "authentication, products, categories, cart, wishlist, "
                                + "orders, payments, reviews, and admin management.")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME))
                .components(new Components().addSecuritySchemes(BEARER_AUTH_SCHEME, bearerScheme));
    }
}