package com.SwitchBoard.PortfolioService.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Curriculum Service API",
                description = "API documentation for the Curriculum Service - Learning Management System (LMS)",
                version = "1.0.0",
                contact = @Contact(
                        name = "Switchboard",
                        email = "support@Switchboard.com",
                        url = "https://www.Switchboard.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)
public class OpenApiConfig {
    // Using annotation-based configuration instead of bean-based
}