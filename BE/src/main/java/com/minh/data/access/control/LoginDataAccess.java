package com.minh.data.access.control;

import com.minh.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public String save(String name, byte[] password) { // unique name
        try {
            User user = repos.userRepository.save(new User(name, password));
            return user.getId().toString();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user exists");
        }
    }
}
