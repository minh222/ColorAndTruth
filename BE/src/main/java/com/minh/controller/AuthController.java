package com.minh.controller;


import com.minh.auth.Jwt;
import com.minh.auth.Verifier;
import com.minh.config.DataAccess;
import com.minh.config.SpringConfig;
import com.minh.data.access.control.LoginDataAccess;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final Semaphore semaphore = new Semaphore(SpringConfig.getCore(), true);

    @PostMapping("/login")
    public String login(String password,
                        String name,
                        @DataAccess LoginDataAccess access) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = access.getUserId(name);
            byte[] stored = access.getStored(userId);
            boolean ok = Verifier.verify(password.toCharArray(), stored);

            if (!ok) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            return Jwt.issue(userId.toString(), 3600);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/register")
    public String register(String password,
                           String name,
                           @DataAccess LoginDataAccess access) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            byte[] verifier = Verifier.creteVerify(password.toCharArray());
            String userId = access.save(name, verifier);
            return Jwt.issue(userId, 3600);
        } finally {
            semaphore.release();
        }
    }


    @GetMapping("/access-token")
    public String getToken(String userId) {
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
