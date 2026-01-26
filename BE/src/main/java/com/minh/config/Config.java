package com.minh.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.minh.business.StringOptimizeImlp;
import com.minh.abtract.String;

import java.util.regex.Pattern;

public final class Config {
    public static final Cache<java.lang.String, Pattern> CACHE =
            Caffeine.newBuilder()
                    .maximumSize(1000)
                    .build();


    public static final String STRING_UTIL = new StringOptimizeImlp(); // StringSpecImpl StringOptimizeImlp
}
