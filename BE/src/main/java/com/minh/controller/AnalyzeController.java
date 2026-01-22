package com.minh.controller;

import static com.minh.apply.ApplyRule.apply;

import com.minh.apply.Output;
import com.minh.thread.SharedExecutor;
import com.minh.thread.WallCpuProbe;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {
    private final Semaphore semaphore = new Semaphore(SharedExecutor.getCORES(), true);

    @PostMapping("/analyze")
    public Output analyze(@RequestParam String original) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return apply(original);
        } finally {
            semaphore.release();
        }
    }
}
