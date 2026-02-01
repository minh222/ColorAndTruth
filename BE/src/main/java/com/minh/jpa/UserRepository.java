package com.minh.jpa;

import com.minh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(Long id);
    User findByName(String name);

    @Transactional
    @Modifying
    @Query("update User u set u.avatar = null where :id = u.id ")
    void update(Long id);
}
