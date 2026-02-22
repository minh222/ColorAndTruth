package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.config.Emitter;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Closure;
import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.minh.config.Config.NOW;
import static com.minh.config.Config.TODAY;

@Service
@DataAccess
public class PostCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public PostCommentDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    @Transactional
    public void postComment(Long userId, String emotion, String claim, Long id, Boolean isDebateClaim, Emitter emitter) {
        Comment newComment = rp.commentRp.save(
                new Comment(userId, emotion, claim, id, isDebateClaim, 0, TODAY(), NOW())
        );
        Long newCommentId = newComment.getId();

        List<Closure> closures = new ArrayList<>(
                Collections.singletonList(new Closure(newCommentId, newCommentId))
        );

        if (id != null) {
            rp.closureRp.findAllByDescendantId(id).forEach(
            c -> closures.add(new Closure(c.getAncestorId(), newCommentId))
            );

            Long receiveUserId = rp.commentRp.getUserId(id);
            List<Long> replyIds = rp.readNotifyRp.getReplyIds(receiveUserId);
            emitter.pushCount(receiveUserId, rp.commentRp.getBadge(receiveUserId, nullIfEmpty(replyIds)));
        }
        rp.closureRp.saveAll(closures);
    }

    private List<Long> nullIfEmpty(List<Long> list) {
        return list.isEmpty() ? null : list;
    }
}