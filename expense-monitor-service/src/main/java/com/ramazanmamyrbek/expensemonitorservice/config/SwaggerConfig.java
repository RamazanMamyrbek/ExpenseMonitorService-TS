package com.ramazanmamyrbek.expensemonitorservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Swagger documentation for expense-monitor-service",
                contact = @Contact(
                        name = "Ramazan Mamyrbek",
                        email = "rama.mamirbek@gmail.com"
                )
        )
)
public class SwaggerConfig {
}
