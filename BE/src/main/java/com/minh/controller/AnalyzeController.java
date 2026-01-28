package com.minh.controller;


import com.minh.apply.ApplyRule;
import com.minh.apply.Output;
import com.minh.config.DataAccess;
import com.minh.data.access.control.LoadCommentCommentDataAccess;
import com.minh.data.access.control.PostCommentDataAccess;
import com.minh.config.SpringConfig;

import com.minh.entity.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/api/v1")
public class AnalyzeController {
    private final Semaphore semaphore = new Semaphore(SpringConfig.getCore(), true);

    @PostMapping("/analyze")
    public Output analyze(@RequestParam String original) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return ApplyRule.exact(original);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/exact")
    public Output exact(@RequestParam String original) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            return ApplyRule.apply(original);
        } finally {
            semaphore.release();
        }
    }

    @PostMapping("/postComment")
    public String postComment(@DataAccess PostCommentDataAccess access,
                              @RequestParam String claim,
                              @RequestParam String emotion) {
        if (!semaphore.tryAcquire()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
        }

        try {
            access.saveComment(emotion, claim);
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

}
