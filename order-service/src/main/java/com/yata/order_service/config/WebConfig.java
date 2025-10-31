package com.yata.order_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4200",                      // Desarrollo
                        "https://yata-frontend.vercel.app",           // Vercel
                        "https://yata-frontend-git-*.vercel.app",     // Preview deployments
                        "https://*.vercel.app",                       // Todos los subdominios de Vercel
                        "https://yata-delivery.com",                  // Dominio principal
                        "https://www.yata-delivery.com"               // Con www
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);  // Cambia a true si usas cookies
    }
}