package com.minh.controller.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LoadCommentResponse {
    private Long id;
    private String emotion;
    private String claim;
    private String name;
    private String avatar;
    private Long count;
    private Long userId;

    public Long getCount(){
        return count - 1;
    }
}
