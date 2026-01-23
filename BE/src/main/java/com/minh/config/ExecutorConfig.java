package com.minh.config;

import com.minh.thread.SharedExecutor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Semaphore;

public class ExecutorConfig {
    @Bean
    public static int getCore() {
        return Runtime.getRuntime().availableProcessors();
    }
}
