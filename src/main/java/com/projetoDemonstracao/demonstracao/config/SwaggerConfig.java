package com.projetoDemonstracao.demonstracao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Demonstracao API")
                        .version("1.0")
                        .description("Aplicação para demonstração dos conhecimentos.")
                        .contact(new Contact()
                                .email("mateuslealhemkemeier@gmail.com")
                                .name("Mateus Leal Hemkemeier")
                        ));
    }
}

