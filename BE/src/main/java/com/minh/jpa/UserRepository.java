package com.minh.jpa;

import com.minh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User getUserById(Long id);
}
