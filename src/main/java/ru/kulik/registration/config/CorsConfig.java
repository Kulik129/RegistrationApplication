package ru.kulik.registration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации для обработки междоменных запросов (CORS).
 */
@Configuration
public class CorsConfig {

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @Value("${allowed.methods}")
    private String allowedMethods;

    @Value("${allowed.headers}")
    private String allowedHeaders;

    /**
     * Конфигурирует CORS для приложения.
     *
     * @return Экземпляр WebMvcConfigurer с настройками CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods(allowedMethods)
                        .allowedHeaders(allowedHeaders);
            }
        };
    }
}
