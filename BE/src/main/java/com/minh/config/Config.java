package com.minh.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.minh.business.StringOptimizeImlp;
import com.minh.business.abtract.StringUtil;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Config {

    public static final StringUtil STRING_UTIL = new StringOptimizeImlp(); // StringSpecImpl StringOptimizeImlp
    public static final LocalDate TODAY = LocalDate.now();
    public static final int CORE = Runtime.getRuntime().availableProcessors();;

    public static final Cache<java.lang.String, Pattern> CACHE =
            Caffeine.newBuilder()
                    .maximumSize(1000)
                    .build();


}
