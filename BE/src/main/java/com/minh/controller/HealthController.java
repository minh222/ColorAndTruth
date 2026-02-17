package com.minh.controller;

import com.minh.apply.rule.ApplyRule;
import com.minh.controller.analyze.response.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

import static com.minh.config.Exception.http;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health/db")
    public String heath() {
        return "ok";
    }
}
