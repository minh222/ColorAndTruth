package com.minh.data.access.control.user;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

@Service
public class GetUserDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public GetUserDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public User getUser(Long id) {
        return repos.userRepository.findById(id);
    }
}
