package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Closure;
import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

        if (id != null) {
            List<Closure> copies = new ArrayList<>();

            r.closureRepository.findAllByDescendantId(id).forEach(
        c -> copies.add(new Closure(c.getAncestorId(), newCommentId))
            );

            r.closureRepository.saveAll(copies);
        }

        r.closureRepository.save(new Closure(newCommentId, newCommentId));
    }
}