package com.minh.jpa;

import com.minh.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c " +
            "from Comment c " +
            "where (:lastId is null or c.id < :lastId) " +
            "and c.parentId is null " +
            "order by c.id desc"
    )
    List<Comment> findNext(Long lastId, Pageable pageable);

    @Query("select max(c.id) from Comment c where c.parentId is null")
    Long findMaxId();

    @Query("select  c.emotion  from Comment c where c.id = :id")
    String getEmotionById(Long id);

    @Query("select max(c.id) from Comment c where (:id is null and c.parentId is null) or c.parentId = :id")
    Long findMaxIdByParentId(Long id);

    @Query("select c " +
            "from Comment c " +
            "where (:lastId is null or c.id < :lastId) " +
            "and ((:id is null and c.parentId is null) or c.parentId = :id )" +
            "order by c.id desc"
    )
    List<Comment> findNextByParentId(Long id, Long lastId, Pageable pageable);
}