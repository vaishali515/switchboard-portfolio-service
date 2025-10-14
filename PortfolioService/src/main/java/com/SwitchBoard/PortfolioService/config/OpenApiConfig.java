package com.SwitchBoard.PortfolioService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.dev-url:http://localhost:8080}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development environment");

        Contact contact = new Contact();
        contact.setName("Portfolio Service Team");
        contact.setEmail("portfolio.service@switchboard.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Portfolio Service API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage portfolios.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}




@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Curriculum Service API",
                description = "API documentation for the Curriculum Service - Learning Management System (LMS)",
                version = "1.0.0",
                contact = @Contact(
                        name = "Lamicons",
                        email = "support@lamicons.com",
                        url = "https://www.lamicons.com"
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