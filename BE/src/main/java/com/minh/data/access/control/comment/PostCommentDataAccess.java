package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Closure;
import com.minh.entity.Comment;
import com.minh.entity.Notify;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void postComment(Long userId, String emotion, String claim, Long id, Boolean isDebateClaim) {
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
            r.notifyRepository.save(new Notify(userId, id));
        }
        r.closureRepository.saveAll(closures);
    }
}