package com.minh.data.access.control;

import com.minh.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentRepos { // danh bạ chứa tất cả repo app
    @Autowired
    public CommentRepository commentRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ViewEmotionRepository viewEmotionRepository;
    @Autowired
    public ClosureRepository closureRepository;
    @Autowired
    public NotifyRepository notifyRepository;

}
