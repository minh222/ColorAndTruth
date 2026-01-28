package com.minh.jpa;

import com.minh.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(
            "select c " +
            "from Comment c " +
            "where (:lastId is null or c.id < :lastId) " +
            "order by c.id desc"
    )
    List<Comment> findNext(@Param("lastId") Long lastId, Pageable pageable);

    @Query("select max(c.id) from Comment c")
    Long findMaxId();
}