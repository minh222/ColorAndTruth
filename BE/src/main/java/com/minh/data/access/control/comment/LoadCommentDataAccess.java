package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.id.CompositeId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoadCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadComment(Long userId, Long lastId, int limit, Integer dayAgo) { // load từ lastId -> lastId + limit
        if (lastId == null) {
            lastId = r.commentRepository.findMaxId() + 1;
        }
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(Math.toIntExact(userId));

        LocalDate today = LocalDate.now().minusDays(dayAgo == null ? 0 : dayAgo);
        List<Long> commentIds = new ArrayList<>();
        ids.forEach(id -> {
            if (id.viewerIsNull())
                commentIds.add(id.getCommentId());
        });

        Pageable pageLimit = PageRequest.of(0, limit);
        return r.commentRepository.loadComment(lastId, ids, today, commentIds, pageLimit);
    }
}