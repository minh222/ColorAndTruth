package com.minh.data.access.control;

import com.minh.jpa.CommentRepository;
import com.minh.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentRepos { // danh bạ chứa tất cả repo app
    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;

}
