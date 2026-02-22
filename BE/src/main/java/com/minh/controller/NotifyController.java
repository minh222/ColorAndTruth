package com.minh.controller;

import com.minh.auth.Jwt;
import com.minh.config.Data;
import com.minh.config.Emitter;
import com.minh.controller.notify.response.NotifyResponse;
import com.minh.data.access.control.notify.CountNotifyDataAccess;
import com.minh.data.access.control.notify.GetNotifyDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.minh.auth.Jwt.getUserId;
import static com.minh.config.Exception.http;

@RestController
@RequestMapping("/api/v1")
public class NotifyController {
    @Autowired
    @Qualifier("spring")
    private Semaphore semaphore;

    @Autowired
    private Emitter emitter;

    @GetMapping("/notify")
    public List<NotifyResponse> getNotify(@Data GetNotifyDataAccess access,
                                          HttpServletRequest request) {
        if (!semaphore.tryAcquire()) {
            throw http(429, "Quá nhiều request, vui lòng thử lại sau");
        }

        try {
            Long userId = getUserId(request);
            return access.getNotify(userId);

        } finally {
            semaphore.release();
        }
    }

    @GetMapping("/notify/count")
    public Integer countNotify(@Data CountNotifyDataAccess access,
                               HttpServletRequest request) {
        return access.getBadge(getUserId(request));
    }

    @GetMapping("/notify/stream")
    public SseEmitter stream(@RequestParam String token) {
        String userId = Jwt.verifyAndGetUserId(token);
        return emitter.connect(Long.valueOf(userId));
    }
}
