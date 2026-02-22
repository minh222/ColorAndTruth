package com.minh.jpa;

import com.minh.entity.ReadNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReadNotifyRepository extends JpaRepository<ReadNotify, Long> {

    @Query("select r.id.replyId from ReadNotify  r where r.id.userId = :userId")
    List<Long> getReplyIds(Long userId);
}
