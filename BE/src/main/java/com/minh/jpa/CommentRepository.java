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
    List<Comment> loadComment(Long lastId, Pageable pageable);

    @Query("select max(c.id) from Comment c where c.parentId is null")
    Long findMaxId();

    @Query("select  c.emotion  from Comment c where :id = c.id")
    String getEmotionById(Long id);

    @Query( "select max(c.id) from Comment c " +
            "where :id = c.parentId or (:id is null and c.parentId is null)")
    Long getMaxChildrenIdById(Long id);

    @Query( "select c " +
            "from Comment c " +
            "where (:id = c.parentId or (:id is null and c.parentId is null))" +
            "and (:lastId > c.id or :lastId is null) " +
            "order by c.id desc"
    )
    List<Comment> loadChildrenById(Long id, Long lastId, Pageable pageable);
}