package com.example.ead2project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("contact@example.com");
        contact.setName("API Support");
        contact.setUrl("https://www.example.com");

        License license = new License()
            .name("Apache 2.0")
            .url("http://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
            .title("Swagger Init API")
            .version("1.0.0")
            .contact(contact)
            .description("This API exposes endpoints for Swagger Demo.")
            .license(license);

        return new OpenAPI().info(info);
    }
} 