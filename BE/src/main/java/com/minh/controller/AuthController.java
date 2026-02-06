package com.minh.controller;

import com.minh.config.DataAccess;
import com.minh.data.access.control.auth.LoginDataAccess;
import com.minh.data.access.control.auth.RegisterDataAccess;
import com.minh.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.Semaphore;

import static com.minh.config.Exception.http;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired @Qualifier("spring")
    private Semaphore semaphore;

    @PostMapping("/login")
    public String login(@DataAccess LoginDataAccess access,
                        String password,
                        String name) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            User user = access.getUser(name);

            if (!user.verifying(password)) {
                throw http(401, "Unauthorized");
            }

            return user.createToken();
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/register")
    public String register(@DataAccess RegisterDataAccess access,
                           String password,
                           String name) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            return access.register(name, password);
        } finally {
            semaphore.release();
        }
    }
}
