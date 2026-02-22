package com.minh.controller.notify.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class NotifyResponse {
    private Long replyId;
    private String commentReply;
    private String comment;
    private String fromUser;
    private String avatar;
    private LocalDateTime time;
}
