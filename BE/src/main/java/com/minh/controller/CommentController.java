package com.minh.controller;

import com.minh.apply.ApplyRule;
import com.minh.apply.Output;
import com.minh.auth.Jwt;
import com.minh.config.DataAccess;
import com.minh.data.access.control.comment.GetEmotionByIdDataAccess;
import com.minh.data.access.control.comment.LoadChildrenByIdDataAccess;
import com.minh.data.access.control.comment.LoadCommentCommentDataAccess;
import com.minh.data.access.control.comment.PostCommentDataAccess;
import com.minh.config.SpringConfig;

import com.minh.entity.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final Semaphore semaphore = new Semaphore(SpringConfig.getCore(), true);

    @PostMapping("/postComment")
    public String postComment(@DataAccess PostCommentDataAccess access,
                              Long id,
                              String claim,
                              String emotion,
                              Boolean isDebateClaim,
                              HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            Long userId = Jwt.getUserId(request);
            access.saveComment(userId, emotion, claim, id, isDebateClaim);
            return "ok";
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment")
    public List<Comment> loadComment(@DataAccess LoadCommentCommentDataAccess access,
                                     Long lastId,
                                     int limit) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return access.loadComment(lastId, limit);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadComment/{id}")
    public String getEmotionById(@DataAccess GetEmotionByIdDataAccess access,
                                 @PathVariable Long id) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return access.getEmotionById(id);
        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/loadChildren")
    public List<Comment> loadChildrenById(@DataAccess LoadChildrenByIdDataAccess access,
                                          Long id,
                                          Long lastId,
                                          int limit) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return access.loadChildrenById(id, lastId, limit);
        } finally {
            semaphore.release();
        }
    }

}
