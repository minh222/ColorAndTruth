package com.minh.data.access.control.auth;


import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.minh.auth.Security.creteVerify;
import static com.minh.config.Exception.http;

@Service
public class RegisterDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public RegisterDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public String register(String name, String password) { // unique name
        try {
            byte[] hashPassword = creteVerify(password.toCharArray());

            User user = r.userRepository.save(
                new User(name, hashPassword)
            );

            return user.createToken();
        } catch (DataIntegrityViolationException e) {
            throw http(409, "user exists");
        }
    }
}
