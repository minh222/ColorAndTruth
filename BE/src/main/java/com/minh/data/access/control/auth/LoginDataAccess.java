package com.minh.data.access.control.auth;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

import static com.minh.config.Exception.http;

@Service
@DataAccess
public class LoginDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public LoginDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public User getUser(String name) {
        User user = rp.userRp.findByName(name);
        if (user == null) {
            throw http(401, "Tài khoản không hợp lệ");
        }
        return user;
    }
}
