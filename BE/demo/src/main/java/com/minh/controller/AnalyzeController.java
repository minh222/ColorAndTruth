package com.minh.controller;

import com.minh.apply.ApplyRule;
import com.minh.apply.Output;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {

    @PostMapping("/analyze")
    public Output analyze(@RequestParam String original) {
        ApplyRule applyRule = new ApplyRule();
        return applyRule.apply(original);
    }
}
