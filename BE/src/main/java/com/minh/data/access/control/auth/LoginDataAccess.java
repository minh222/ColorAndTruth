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

    public byte[] getStored(Long id) {
        User user = r.userRepository.findById(id);
        return user.getPassword();
    }

    public Long getUserId(String name) {
        User user = r.userRepository.findByName(name);
        return user.getId();
    }
}
