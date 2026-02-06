package com.minh.jpa;

import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.Comment;
import com.minh.entity.id.CompositeId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query( "select new com.minh.controller.comment.response.LoadCommentResponse" +
            "(c.id, c.emotion, c.claim, u.name, u.avatar, count(cl.ancestorId), u.id, c.time, c.isDebateClaim, v.id.viewerId) " +
            "from Comment c " +
            "join User u on c.userId = u.id " +
            "join Closure cl on c.id = cl.ancestorId  " +
            "left join ViewEmotion v on c.id = v.id.commentId  " +
            "where (:lastId is null or c.id < :lastId) " +
            "and c.parentId is null " +
            "and c.date = :today " +
            "and v.id in :ids " +
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id,  v.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadComment(List<CompositeId> ids, Long lastId, LocalDate today, Pageable pageable);

    @Query( "select new com.minh.entity.id.CompositeId ( " +
            "  v.id.commentId, " +
            "  coalesce( " +
            "    max(case when v.id.viewerId = :userId then v.id.viewerId else null end), " +
            "    min(v.id.viewerId) " +
            "  ) )" +
            "from ViewEmotion v " +
            "group by v.id.commentId ")
    List<CompositeId> getCompositeIdsByUserId(Integer userId);

    @Query( "select new com.minh.controller.comment.response.LoadCommentResponse" +
            "(c.id, c.emotion, c.claim, u.name, u.avatar, count (cl.ancestorId), u.id, c.time,c.isDebateClaim, v.id.viewerId) " +
            "from Comment c " +
            "join User u on c.userId = u.id " +
            "join Closure cl on c.id = cl.ancestorId   " +
            "left join ViewEmotion v on c.id = v.id.commentId  " +
            "where (:id = c.parentId or (:id is null and c.parentId is null))" +
            "and (:lastId > c.id or :lastId is null) " +
            "and cl.ancestorId <> :id " +
            "and  v.id in :ids " +
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id,  v.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadChildrenById(List<CompositeId> ids, Long id, Long lastId, Pageable pageable);

    @Query("select max(c.id) from Comment c where c.parentId is null")
    Long findMaxId();

    @Query( "select max(c.id) from Comment c " +
            "where :id = c.parentId or (:id is null and c.parentId is null)")
    Long getMaxChildrenIdById(Long id);

    @Query("select count(c.id) from Comment c where c.parentId = (select x.parentId from Comment x where c.id = :id) ")
    Integer getCountById(Long id);
}