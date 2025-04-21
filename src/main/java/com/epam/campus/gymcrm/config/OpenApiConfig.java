package com.epam.campus.gymcrm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Spring Boot Gym CRM App",
        version = "1.0",
        description = "Spring Boot Gym CRM API",
        termsOfService = "http://swagger.io/terms/",
        contact = @Contact(
            name = "Maria Cabrales",
            email = "mafecaja@gmail.com"),
        license = @License(name = "Apache 2.0", url = "http://springdoc.org"),
        summary = "Spring Boot Gym CRM API"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {
}
