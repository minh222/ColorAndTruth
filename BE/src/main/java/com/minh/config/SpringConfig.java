package com.minh.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Semaphore;
import static com.minh.config.Config.CORE;

@EnableCaching
@Configuration
public class SpringConfig {

    @Bean("spring")
    public Semaphore getSemaphore() {
        return new Semaphore( CORE, true);
    }

}
