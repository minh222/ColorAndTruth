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

@Service
@DataAccess
public class LoadChildrenCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public LoadChildrenCommentDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public List<LoadCommentResponse> loadChildrenComment(Long id, Long lastId, int limit, Long userId, Integer dayAgo) {
        Long maxId = rp.commentRp.getMaxChildrenIdById(id);
        List<ViewEmotionId> ids = rp.commentRp.getCompositeIdsByUserId(userId, getDate(dayAgo));

        List<LoadCommentResponse> res = rp.commentRp.loadChildrenById(
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

    private List<Long> getCommentIds(List<ViewEmotionId> ids) {
        return ids.stream().filter(ViewEmotionId::viewerIsNull).map(ViewEmotionId::getCommentId).collect(Collectors.toList());
    }

    private PageRequest getPageable(int limit) {
        return PageRequest.of(0, limit);
    }

    private LocalDate getDate(Integer days) {
        return TODAY().minusDays(days == null ? 0 : days);
    }

}