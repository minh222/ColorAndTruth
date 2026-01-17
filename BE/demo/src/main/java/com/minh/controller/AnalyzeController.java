package com.minh.controller;

import com.minh.apply.Output;
import org.springframework.web.bind.annotation.*;

import static com.minh.apply.ApplyRule.apply;


@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {

    @PostMapping("/analyze")
    public Output analyze(@RequestParam String original) {
        return apply(original);
    }
}
