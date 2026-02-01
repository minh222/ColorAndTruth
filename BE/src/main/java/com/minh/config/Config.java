package com.minh.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.minh.business.StringOptimizeImlp;
import com.minh.business.abtract.String;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Config {
    public static final Cache<java.lang.String, Pattern> CACHE =
            Caffeine.newBuilder()
                    .maximumSize(1000)
                    .build();

    public static final String STRING_UTIL = new StringOptimizeImlp(); // StringSpecImpl StringOptimizeImlp

    public static final LocalDate today = LocalDate.now();
}
