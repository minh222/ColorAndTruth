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
import java.util.stream.Collectors;

@Service
public class LoadChildrenCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadChildrenCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadChildrenComment(Long id, Long lastId, int limit, Long userId) {
        Long maxId = r.commentRepository.getMaxChildrenIdById(id);
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(userId);

        List<LoadCommentResponse> res = r.commentRepository.loadChildrenById(
                id,
                getLastId(maxId, lastId),
                ids,
                getCommentIds(ids),
                getPageable(limit)
        );

        res.forEach(r -> r.alwaysTrueWhenDifference(userId));

        return res;
    }

    // Helper
    private Long getLastId(Long maxId, Long lastId) {
        if (lastId != null) return lastId;
        return maxId == null ? null : maxId + 1;
    }

    private List<Long> getCommentIds(List<CompositeId> ids) {
        return ids.stream()
                .filter(CompositeId::viewerIsNull)
                .map(CompositeId::getCommentId)
                .collect(Collectors.toList());
    }

    private PageRequest getPageable(int limit) {
        return PageRequest.of(0, limit);
    }

}