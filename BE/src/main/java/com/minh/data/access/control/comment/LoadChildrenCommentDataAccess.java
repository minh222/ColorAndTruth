package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.id.CompositeId;
import com.minh.thread.ExecutorUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class LoadChildrenCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadChildrenCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadChildrenComment(Long id, Long lastId, int limit, Long userId) {
        if (lastId == null) {
            Long maxId = r.commentRepository.getMaxChildrenIdById(id);
            lastId = maxId == null ? null : maxId + 1;
        }
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(Math.toIntExact(userId));

        List<Long> commentIds = new ArrayList<>();
        ids.forEach(e -> {
            if (e.viewerIsNull())
                commentIds.add(e.getCommentId());
        });

        Pageable pageLimit = PageRequest.of(0, limit);
        return r.commentRepository.loadChildrenById(id, lastId, ids, commentIds, pageLimit);
    }
}