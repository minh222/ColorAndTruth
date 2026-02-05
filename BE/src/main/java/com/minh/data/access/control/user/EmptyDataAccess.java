package com.minh.data.access.control.user;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

@Service
public class EmptyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public EmptyDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public User getUser(Long id) {
        return r.userRepository.findById(id);
    }

    public void updateUser(User user) {
        r.userRepository.save(user);
    }
}
