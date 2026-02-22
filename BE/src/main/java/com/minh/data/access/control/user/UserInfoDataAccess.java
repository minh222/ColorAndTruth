package com.minh.data.access.control.user;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@DataAccess
public class UserInfoDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public UserInfoDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public User getUser(Long id) {
        return rp.userRp.getUserById(id);
    }
}
