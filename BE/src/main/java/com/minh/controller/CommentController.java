package com.minh.controller;

import com.minh.config.DataAccess;
import com.minh.data.access.control.comment.*;
import com.minh.controller.comment.response.LoadCommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.minh.auth.Jwt.getUserId;
import static com.minh.config.Exception.http;


@RestController
@RequestMapping("/api/v1")
public class CommentController {

    @Autowired @Qualifier("spring")
    private Semaphore semaphore;

    @PostMapping("/postComment")
    public String postComment(@DataAccess PostCommentDataAccess access,
                              Long id,
                              String claim,
                              String emotion,
                              Boolean isDebateClaim,
                              HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            Long userId = getUserId(request);
            access.saveComment(userId, emotion, claim, id, isDebateClaim);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment")
    public List<LoadCommentResponse> loadComment(@DataAccess LoadCommentDataAccess access,
                                                 Long lastId,
                                                 Integer dayAgo,
                                                 int limit) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            return access.loadComment(lastId, limit, dayAgo);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment/{id}")
    public String getEmotionById(@DataAccess GetEmotionByIdDataAccess access,
                                 @PathVariable Long id,
                                 HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            Long userId = getUserId(request);
            return access.getEmotionById(id, userId);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadChildren")
    public List<LoadCommentResponse> loadChildrenById(@DataAccess LoadChildrenByIdDataAccess access,
                                          Long id,
                                          Long lastId,
                                          int limit) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            return access.loadChildrenById(id, lastId, limit);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/remove/{id}")
    public String removeComment(@DataAccess RemoveCommentDataAccess access,
                                @PathVariable Long id) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Too many requests");
        }

        try {
            access.removeComment(id);
            return "ok";
        } finally {
            semaphore.release();
        }
    }
}
