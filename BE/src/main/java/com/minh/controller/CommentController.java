package com.minh.controller;

import com.minh.auth.Jwt;
import com.minh.config.Data;
import com.minh.controller.comment.response.GetEmotionResponse;
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
    public String postComment(@Data PostCommentDataAccess access,
                              Long id,
                              String claim,
                              String emotion,
                              Boolean isDebateClaim,
                              HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            Long userId = getUserId(request);
            access.postComment(userId, emotion, claim, id, isDebateClaim);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment")
    public List<LoadCommentResponse> loadComment(@Data LoadCommentDataAccess access,
                                                 Long lastId,
                                                 Integer dayAgo,
                                                 int limit,
                                                 HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            Long userId = Jwt.getUserId(request);
            return access.loadComment(userId, lastId, limit, dayAgo);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadChildren")
    public List<LoadCommentResponse> loadChildrenComment(@Data LoadChildrenCommentDataAccess access,
                                                         Long id,
                                                         Long lastId,
                                                         int limit,
                                                         HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            Long userId = Jwt.getUserId(request);
            return access.loadChildrenComment(id, lastId, limit,userId);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment/{id}")
    public GetEmotionResponse seenEmotion(@Data SeenEmotionDataAccess access,
                                          @PathVariable Long id,
                                          HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            Long userId = getUserId(request);
            return access.seenEmotion(id, userId);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/remove/{id}")
    public String removeComment(@Data RemoveCommentDataAccess access,
                                @PathVariable Long id) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            access.removeComment(id);
            return "ok";
        } finally {
            semaphore.release();
        }
    }
}
