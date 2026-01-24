package com.minh.config;

import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ExecutorConfig {
    @Bean
    public static int getCore() {
        return Runtime.getRuntime().availableProcessors();
    }
}
