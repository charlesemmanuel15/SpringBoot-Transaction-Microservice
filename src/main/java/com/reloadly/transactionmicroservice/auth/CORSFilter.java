package com.reloadly.transactionmicroservice.auth;

import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

public class CORSFilter implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    }

}