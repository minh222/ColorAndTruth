package com.minh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class RegexCache {
    private final Map<String, Pattern> cache = new ConcurrentHashMap<>();

    public Pattern get(String target) {
        return cache.computeIfAbsent(
                target,
                t -> Pattern.compile("\\b" + Pattern.quote(t) + "\\b")
        );
    }
}
