package com.minh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Semaphore;
import static com.minh.config.Config.CORE;

@Configuration
public class SpringConfig {

    @Bean("spring")
    public Semaphore getSemaphore() {
        return new Semaphore( CORE, true);
    }

}
