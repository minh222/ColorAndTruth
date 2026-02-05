package com.minh.data.access.control.auth;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

@Service
public class LoginDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoginDataAccess(CurrentRepos repos) {
        this.r = repos;
    }


    public User getUser(String name) {
        return r.userRepository.findByName(name);
    }
}
