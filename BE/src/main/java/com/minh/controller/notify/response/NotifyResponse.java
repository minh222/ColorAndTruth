package com.minh.controller.notify.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class NotifyResponse {
    private String comment;
    private String commentReply;
    private String fromUser;
    private String avatar;
}
