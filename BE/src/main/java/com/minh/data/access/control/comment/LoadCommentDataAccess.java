package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.id.CompositeId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoadCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadComment(Long userId, Long lastId, int limit, Integer dayAgo) { // load từ lastId -> lastId + limit
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(userId);

        List<LoadCommentResponse> res = r.commentRepository.loadComment(
                lastId == null ? r.commentRepository.findMaxId() + 1 : lastId,
                ids,
                LocalDate.now().minusDays(dayAgo == null ? 0 : dayAgo),
                ids.stream().filter(CompositeId::viewerIsNull).map(CompositeId::getCommentId).collect(Collectors.toList()),
                PageRequest.of(0, limit)
        );

        res.forEach(r -> r.alwaysTrueWhenDifference(userId));

        return res;
    }
}