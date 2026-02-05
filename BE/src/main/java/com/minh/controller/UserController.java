package com.minh.controller;

import com.minh.auth.Jwt;
import com.minh.config.DataAccess;

import com.minh.config.SpringConfig;
import com.minh.data.access.control.user.EmptyDataAccess;
import com.minh.data.access.control.user.GetUserDataAccess;
import com.minh.data.access.control.user.UploadDataAccess;
import com.minh.entity.User;
import com.minh.upload.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Semaphore;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    @Qualifier("spring")
    private Semaphore semaphore;

    @PostMapping("/upload")
    public String upload(@DataAccess CloudinaryService cloudinaryService,
                         @DataAccess UploadDataAccess access,
                         @RequestParam MultipartFile file,
                         HttpServletRequest request) throws Exception {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = Jwt.getUserId(request);
            User user = access.getUser(userId);

            String link = cloudinaryService.upload(file.getBytes(), String.valueOf(userId));

            user.setAvatar(link);
            access.updateUser(user);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/empty-avatar")
    public String emptyAvatar(@DataAccess EmptyDataAccess access,
                              HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = Jwt.getUserId(request);
            User user = access.getUser(userId);
            user.resetCountToday();

            if (user.getAvatarChangeCount() >= 20) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Mỗi ngày chỉ được xóa avatar 20 lần");
            }

            user.emptyAvatarAndIncreaseCounter();
            access.updateUser(user);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/getUser")
    public User getUser(@DataAccess GetUserDataAccess access,
                        HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = Jwt.getUserId(request);
            return access.getUser(userId);
        } finally {
            semaphore.release();
        }
    }

}
