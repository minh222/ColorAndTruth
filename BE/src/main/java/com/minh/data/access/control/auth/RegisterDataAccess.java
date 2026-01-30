package com.minh.data.access.control.auth;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegisterDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public RegisterDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public String save(String name, byte[] password) { // unique name
        try {
            User user = repos.userRepository.save(new User(name, password));
            return user.getId().toString();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user exists");
        }
    }
}
