package com.minh.jpa;

import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.Comment;
import com.minh.entity.id.CompositeId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
            "and c.date = :date " +
            "and (v.id in :ids or c.id in :commentIds) " + // filter theo ids : đã collapse đúng, hoặc theo :commentIds nếu viewer = null <=> 0 dòng ko collapse
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id,  v.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadComment(Long lastId, List<CompositeId> ids, LocalDate date, List<Long>  commentIds, Pageable pageable);

    /*   ViewerId: collapse priority
         Nếu nhiều dòng có userId thì ưu tiên lấy dòng có userId,
         Nếu nhiều dòng ko có userId thì lấy dòng max
         Nếu ko có dòng nào thì viewerId = null */
    @Query( "select new com.minh.entity.id.CompositeId ( " +
            "  c.id, " +
            "  coalesce( " +
            "    max(case when v.id.viewerId = :userId then v.id.viewerId else null end ), " +
            "    max(v.id.viewerId) " +
            "  ))" +
            "from Comment c " +
            "left join ViewEmotion v on c.id = v.id.commentId " +
            "group by c.id ")
    List<CompositeId> getCompositeIdsByUserId(Long userId);

    @Query( "select new com.minh.controller.comment.response.LoadCommentResponse" +
            "(c.id, c.emotion, c.claim, u.name, u.avatar, count (cl.ancestorId), u.id, c.time,c.isDebateClaim, v.id.viewerId) " +
            "from Comment c " +
            "join User u on c.userId = u.id " +
            "join Closure cl on c.id = cl.ancestorId   " +
            "left join ViewEmotion v on c.id = v.id.commentId  " +
            "where (:id = c.parentId or (:id is null and c.parentId is null))" +
            "and (:lastId > c.id or :lastId is null) " +
            "and cl.ancestorId <> :id " +
            "and (v.id in :ids or c.id in :commentIds) " +
            "group by c.id, c.emotion, c.claim, u.name, u.avatar, u.id,  v.id " +
            "order by c.id desc"
    )
    List<LoadCommentResponse> loadChildrenById(Long id, Long lastId, List<CompositeId> ids, List<Long> commentIds, Pageable pageable);

    @Query("select max(c.id) from Comment c where c.parentId is null")
    Long findMaxId();

    @Query( "select max(c.id) from Comment c " +
            "where :id = c.parentId or (:id is null and c.parentId is null)")
    Long getMaxChildrenIdById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.id in :descendantIds ")
    Integer deleteAllByIdIn(List<Long> descendantIds);
}