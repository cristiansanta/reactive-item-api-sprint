package com.mindhub.webfluxdemo.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reactive Item API")
                        .version("1.0")
                        .description("A reactive API for managing items using Spring WebFlux and R2DBC"));
    }
}