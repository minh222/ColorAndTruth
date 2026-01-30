package com.minh.controller;

import com.minh.apply.ApplyRule;
import com.minh.apply.Output;
import com.minh.config.SpringConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {
    private final Semaphore semaphore = new Semaphore(SpringConfig.getCore(), true);

    @PostMapping("/analyze")
    public Output analyze(String original) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return ApplyRule.exact(original);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/exact")
    public Output exact(String original) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return ApplyRule.apply(original);
        } finally {
            semaphore.release();
        }
    }
}
