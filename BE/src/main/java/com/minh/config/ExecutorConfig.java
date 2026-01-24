package com.minh.config;

import org.springframework.context.annotation.Bean;

public class ExecutorConfig {
    @Bean
    public static int getCore() {
        return Runtime.getRuntime().availableProcessors();
    }
}
