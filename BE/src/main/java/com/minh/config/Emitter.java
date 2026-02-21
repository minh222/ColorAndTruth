package com.minh.config;


import com.minh.data.access.control.comment.PostCommentDataAccess;
import com.minh.data.access.control.notify.GetNotifyDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Emitter {

    @Autowired
    PostCommentDataAccess access;

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long userId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> emitters.remove(userId));

        pushCount(userId, 0);

        return emitter;
    }

    public void pushCount(Long userId, int count) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) return;

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("notify-count")
                            .data(count)
            );
        } catch (IOException e) {
            emitters.remove(userId);
        }
    }
}
