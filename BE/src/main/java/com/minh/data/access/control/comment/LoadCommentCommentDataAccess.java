package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.data.access.control.comment.response.LoadCommentResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoadCommentCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public LoadCommentCommentDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public List<LoadCommentResponse> loadComment(Long lastId, int limit, Integer dayAgo) { // load từ lastId -> lastId + limit
        if (lastId == null) {
            lastId = repos.commentRepository.findMaxId() + 1;
        }
        LocalDate today = LocalDate.now().minusDays(dayAgo == null ? 0 : dayAgo);
        Pageable pageLimit = PageRequest.of(0, limit);

        return repos.commentRepository.loadComment(lastId, today, pageLimit);
    }
}