package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.controller.comment.response.LoadCommentResponse;
import com.minh.entity.composite.id.ViewEmotionId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.minh.config.Config.TODAY;
import static com.minh.config.Exception.http;

@Service
@DataAccess
public class LoadCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadComment(Long userId, Long lastId, int limit, Integer days) { // load từ lastId -> lastId + limit
        Long maxId = r.commentRepository.findMaxId().orElseThrow(
                () -> http(502, "No data available")
        );

        List<ViewEmotionId> ids = r.commentRepository.getCompositeIdsByUserId(userId);

        List<LoadCommentResponse> res = r.commentRepository.loadComment(
                getLastId(maxId, lastId),
                ids,
                getDate(days),
                getCommentIds(ids),
                getPageable(limit)
        );

        res.forEach(r -> r.alwaysTrueWhenDifference(userId));

        return res;
    }

    // Helper
    private LocalDate getDate(Integer days) {
        return TODAY().minusDays(days == null ? 0 : days);
    }

    private Long getLastId(Long maxId, Long lastId) {
        return lastId == null ? maxId + 1 : lastId;
    }

    private List<Long> getCommentIds(List<ViewEmotionId> ids) {
        return ids.stream().filter(ViewEmotionId::viewerIsNull).map(ViewEmotionId::getCommentId).collect(Collectors.toList());
    }

    private PageRequest getPageable(int limit) {
        return PageRequest.of(0, limit);
    }
}