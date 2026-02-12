package com.minh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Semaphore;
import static com.minh.config.Config.CORE;

@Configuration
public class SpringConfig {

    @Bean("spring")
    public Semaphore getSemaphore() {
        return new Semaphore( CORE*2, true);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://colorandtruth1.onrender.com")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

}
