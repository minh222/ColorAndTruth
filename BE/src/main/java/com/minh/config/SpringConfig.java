package com.minh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    @Bean
    public static int getCore() {
        return Runtime.getRuntime().availableProcessors();
    }
}
