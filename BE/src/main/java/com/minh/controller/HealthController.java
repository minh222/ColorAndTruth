package com.minh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/health/db")
    public ResponseEntity<Map<String, Object>> healthDb() {
        Map<String, Object> result = new HashMap<>();
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            result.put("status", "UP");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "DOWN");
            result.put("error", e.getMessage());
            return ResponseEntity.ok(result); // vẫn 200
        }
    }

}
