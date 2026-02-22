package com.minh.data.access.control;

import com.minh.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentRepos { // danh bạ chứa tất cả repo app
    @Autowired
    public CommentRepository commentRp;
    @Autowired
    public UserRepository userRp;
    @Autowired
    public ViewEmotionRepository viewEmotionRp;
    @Autowired
    public ClosureRepository closureRp;
    @Autowired
    public ReadNotifyRepository readNotifyRp;
}
