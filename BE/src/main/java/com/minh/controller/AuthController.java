package com.minh.controller;


import com.minh.auth.Jwt;
import com.minh.auth.Verifier;
import com.minh.config.DataAccess;
import com.minh.config.SpringConfig;
import com.minh.data.access.control.LoginDataAccess;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final Semaphore semaphore = new Semaphore(SpringConfig.getCore(), true);

    @PostMapping("/login")
    public boolean login(@RequestParam String password,
                         @DataAccess LoginDataAccess loginDataAccess,
                         HttpServletRequest httpRequest) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = Jwt.getUserId(httpRequest);
            byte[] stored = loginDataAccess.getStored(userId);
            return Verifier.verify(password.toCharArray(), stored);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String password,
                           @RequestParam String name,
                           @DataAccess LoginDataAccess loginDataAccess) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            byte[] verifier = Verifier.creteVerify(password.toCharArray());
            String userId = loginDataAccess.save(name, verifier);
            return Jwt.issue(userId, 3600);
        } finally {
            semaphore.release();
        }
    }


    @GetMapping("/access-token")
    public String getToken(@RequestParam String userId) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return Jwt.issue(userId, 3600);
        } finally {
            semaphore.release();
        }
    }
}
