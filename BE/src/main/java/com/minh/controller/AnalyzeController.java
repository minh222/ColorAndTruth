package com.minh.controller;

import com.minh.apply.rule.ApplyRule;
import com.minh.controller.analyze.response.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Semaphore;

import static com.minh.config.Exception.*;

@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {

    @Autowired
    @Qualifier("spring")
    private Semaphore semaphore;

    @PostMapping("/analyze")
    public AnalyzeResponse analyze(String original) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            return ApplyRule.exact(original);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/exact")
    public AnalyzeResponse exact(String original) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            return ApplyRule.apply(original);
        } finally {
            semaphore.release();
        }
    }
}
