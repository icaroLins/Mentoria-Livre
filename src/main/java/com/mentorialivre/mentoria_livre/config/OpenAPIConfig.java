package com.mentorialivre.mentoria_livre.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
   private static final String SECURITY_SCHEME = "bearerAuth";

   @Bean
    public OpenAPI openAPI() {
        return  new OpenAPI()
                .info( new Info()
                        .title("Mentoria Livre")
                        .version("1.0")
                        .description("API do Mentoria Livre"))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME));
    }

    @Bean
    public GroupedOpenApi authAPI(){
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userAPI(){
       return GroupedOpenApi.builder()
               .group("users")
               .pathsToMatch("/users/**")
               .build();
    }

}
