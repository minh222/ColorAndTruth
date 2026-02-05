package com.minh.controller;


import com.minh.auth.Jwt;
import com.minh.auth.Verifier;
import com.minh.config.DataAccess;
import com.minh.config.SpringConfig;
import com.minh.data.access.control.auth.LoginDataAccess;
import com.minh.data.access.control.auth.RegisterDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.concurrent.Semaphore;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    @Qualifier("spring")
    private Semaphore semaphore;

    @PostMapping("/login")
    public String login(@DataAccess LoginDataAccess access,
                        String password,
                        String name) {
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

            return Jwt.issue(userId.toString(), 10000);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/register")
    public String register(@DataAccess RegisterDataAccess access,
                           String password,
                           String name) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            byte[] verifier = Verifier.creteVerify(password.toCharArray());
            String userId = access.save(name, verifier);
            return Jwt.issue(userId, 10000);
        } finally {
            semaphore.release();
        }
    }


}
