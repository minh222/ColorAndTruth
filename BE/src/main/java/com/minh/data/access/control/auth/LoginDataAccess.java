package com.minh.data.access.control.auth;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

@Service
public class LoginDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public LoginDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public byte[] getStored(Long id) {
        User user = repos.userRepository.findById(id);
        return user.getPassword();
    }

    public Long getUserId(String name) {
        User user = repos.userRepository.findByName(name);
        return user.getId();
    }

    public User getUser(Long id) {
        return repos.userRepository.findById(id);
    }

    public void updateUser(User user) {
        repos.userRepository.save(user);
    }
}
