package com.example.foodx_be.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =
        @Info(
                contact =
                @Contact(
                        name = "Le Trong Duong",
                        email = "duongle157.work@gmail.com",
                        url = "https://github.com/DwngLe"),
                title = "OpenApi specification - Le Trong Duong",
                description = "OpenApi documentation for Food X",
                version = "1.0"),
        servers = {
                @Server(description = "Local ENV", url = "http://localhost:8080/api"),
                @Server(description = "PROD ENV", url = "https://foodxbe-production.up.railway.app/api")
        })
@SecurityScheme(
        name = "bearAuth",
        description = "JWT auth desciption",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
