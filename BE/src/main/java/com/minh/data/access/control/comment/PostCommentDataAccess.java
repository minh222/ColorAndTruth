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
    public final CurrentRepos r;

    public PostCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public void postComment(Long userId, String emotion, String claim, Long id, Boolean isDebateClaim, Emitter emitter) {
        Comment newComment = r.commentRepository.save(
                new Comment(userId, emotion, claim, id, isDebateClaim, 0, TODAY(), NOW())
        );
        Long newCommentId = newComment.getId();

        List<Closure> closures = new ArrayList<>(
                Collections.singletonList(new Closure(newCommentId, newCommentId))
        );

        if (id != null) {
            r.closureRepository.findAllByDescendantId(id).forEach(
            c -> closures.add(new Closure(c.getAncestorId(), newCommentId))
            );

            Long receiveUserId = r.commentRepository.getUserId(id);
            emitter.pushCount(receiveUserId, r.commentRepository.getBadge(receiveUserId));
        }
        r.closureRepository.saveAll(closures);
    }
}