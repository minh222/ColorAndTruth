package com.minh.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.minh.business.StringOptimizeImlp;
import com.minh.abtract.String;
import com.minh.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class Config {
    public static final Cache<java.lang.String, Pattern> CACHE =
            Caffeine.newBuilder()
                    .maximumSize(1000)
                    .build();

    public static final String STRING_UTIL = new StringOptimizeImlp(); // StringSpecImpl StringOptimizeImlp
}
