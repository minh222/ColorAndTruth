package com.minh.controller;

import com.minh.config.Data;

import com.minh.data.access.control.user.EmptyDataAccess;
import com.minh.data.access.control.user.UserInfoDataAccess;
import com.minh.data.access.control.user.UploadDataAccess;
import com.minh.entity.User;
import com.minh.upload.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Semaphore;

import static com.minh.auth.Jwt.getUserId;
import static com.minh.config.Exception.http;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    @Qualifier("spring")
    private Semaphore semaphore;

    @Autowired
    private CloudinaryService cloud;

    @PostMapping("/upload")
    public String upload(@Data UploadDataAccess access,
                         @RequestParam MultipartFile file,
                         HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            Long userId = getUserId(request);
            String link = cloud.upload(file, userId.toString());
            access.updateAvatar(userId, link);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/empty-avatar")
    public String emptyAvatar(@Data EmptyDataAccess access,
                              HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            Long userId = getUserId(request);
            access.emptyAvatar(userId);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/getUser")
    public User userInfo(@Data UserInfoDataAccess access,
                         HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            Long userId = getUserId(request);
            return access.getUser(userId);
        } finally {
            semaphore.release();
        }
    }

}
