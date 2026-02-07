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
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(userId);

        return r.commentRepository.loadChildrenById(
                id,
                lastId != null ? lastId : ((lastId = r.commentRepository.getMaxChildrenIdById(id)) == null ? null : lastId + 1),
                ids,
                ids.stream().filter(CompositeId::viewerIsNull).map(CompositeId::getCommentId).collect(Collectors.toList()),
                PageRequest.of(0, limit)
        );
    }
}