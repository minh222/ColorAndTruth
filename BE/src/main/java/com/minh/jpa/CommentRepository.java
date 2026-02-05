package com.minh.jpa;

import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new com.minh.controller.comment.response.LoadCommentResponse" +
            "(c.id, c.emotion, c.claim, u.name, u.avatar, count(cl.ancestorId), u.id) " +
            "from Comment c join User u on c.userId = u.id " +
            "join Closure cl on c.id = cl.ancestorId   " +
            "where (:lastId is null or c.id < :lastId) " +
            "and c.parentId is null " +
            "and c.date = :today " +
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadComment(Long lastId, LocalDate today, Pageable pageable);

    @Query("select max(c.id) from Comment c where c.parentId is null")
    Long findMaxId();

    @Query("select  c.emotion  from Comment c where :id = c.id")
    String getEmotionById(Long id);

    @Query( "select max(c.id) from Comment c " +
            "where :id = c.parentId or (:id is null and c.parentId is null)")
    Long getMaxChildrenIdById(Long id);

    @Query("select new com.minh.controller.comment.response.LoadCommentResponse" +
            "(c.id, c.emotion, c.claim, u.name, u.avatar, count (cl.ancestorId), u.id) " +
            "from Comment c join User u on c.userId = u.id " +
            "join Closure cl on c.id = cl.ancestorId   " +
            "where (:id = c.parentId or (:id is null and c.parentId is null))" +
            "and (:lastId > c.id or :lastId is null) " +
            "and cl.ancestorId <> :id " +
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadChildrenById(Long id, Long lastId, Pageable pageable);
}