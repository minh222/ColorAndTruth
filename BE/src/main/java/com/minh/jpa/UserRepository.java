package com.minh.jpa;

import com.minh.entity.Comment;
import com.minh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(Long id);
}
