package com.org.auth.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "Auth Server API", version = "1.0"),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
//@SecurityScheme(
//        name = "oauth2",
//        type = SecuritySchemeType.OAUTH2,
//        flows = @OAuthFlows(
//                clientCredentials = @OAuthFlow(
//                        tokenUrl = "https://localhost:8081/oauth2/token",
//                        scopes = {
//                                @OAuthScope(name = "read", description = "Read access"),
//                                @OAuthScope(name = "create", description = "Create access"),
//                                @OAuthScope(name = "update", description = "Update access"),
//                                @OAuthScope(name = "delete", description = "Delete access")
//                        }
//                ),
//                password = @OAuthFlow(
//                        tokenUrl = "https://localhost:8081/oauth2/token",
//                        scopes = {
//                                @OAuthScope(name = "read", description = "Read access"),
//                                @OAuthScope(name = "create", description = "Create access"),
//                                @OAuthScope(name = "update", description = "Update access"),
//                                @OAuthScope(name = "delete", description = "Delete access")
//                        }
//                )
//        )
//)
public class AppConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 1. Don't fail on unknown fields in incoming JSON
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 2. Pretty print JSON output
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 3. Include non-null fields only
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 4. Support Java 8 date/time types (LocalDate, LocalDateTime)
        mapper.registerModule(new JavaTimeModule());
        // 5. Prevent timestamps for dates (write ISO strings instead)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 6. Optional: Change naming strategy (e.g., camelCase → snake_case) user_name maps to userName (because of SNAKE_CASE).
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper;
    }

    @Bean
    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Authentication Server API")
//                        .description("API for generating OAuth2 tokens")
//                        .version("1.0"))
//                .components(new Components().addSecuritySchemes("oauth2", new io.swagger.v3.oas.models.security.SecurityScheme()
//                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2)
//                        .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
//                                .clientCredentials(new io.swagger.v3.oas.models.security.OAuthFlow()
//                                        .tokenUrl("https://localhost:8081/oauth2/token")
//                                        .scopes(new Scopes()
//                                                .addString("read", "Read access")
//                                                .addString("create", "Create access")
//                                                .addString("update", "Update access")
//                                                .addString("delete", "Delete access")
//                                        )))))
//                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2"));
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Authentication Server API")
                        .description("API for generating and using JWT tokens")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("BearerAuth"));
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(8081); // MUST match NGINX config
        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
