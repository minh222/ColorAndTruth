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
        Long maxId = r.commentRepository.findMaxId();
        List<CompositeId> ids = r.commentRepository.getCompositeIdsByUserId(userId);

        List<LoadCommentResponse> res = r.commentRepository.loadComment(
                getLastId(maxId, lastId),
                ids,
                getDate(dayAgo),
                getCommentIds(ids),
                getPageable(limit)
        );

        res.forEach(r -> r.alwaysTrueWhenDifference(userId));

        return res;
    }

    // Helper
    private LocalDate getDate(Integer dayAgo) {
        return LocalDate.now().minusDays(dayAgo == null ? 0 : dayAgo);
    }

    private Long getLastId(Long maxId, Long lastId) {
        return lastId == null ? maxId + 1 : lastId;
    }

    private List<Long> getCommentIds(List<CompositeId> ids) {
        return ids.stream().filter(CompositeId::viewerIsNull).map(CompositeId::getCommentId).collect(Collectors.toList());
    }

    private PageRequest getPageable(int limit) {
        return PageRequest.of(0, limit);
    }
}