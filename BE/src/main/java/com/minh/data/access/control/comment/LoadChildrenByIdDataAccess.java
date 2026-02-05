package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.controller.comment.response.LoadCommentResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadChildrenByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public LoadChildrenByIdDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<LoadCommentResponse> loadChildrenById(Long id, Long lastId, int limit) {
        if (lastId == null) {
            Long maxId = r.commentRepository.getMaxChildrenIdById(id);
            lastId = maxId == null ? null : maxId + 1;
        }
        Pageable pageLimit = PageRequest.of(0, limit);

        return r.commentRepository.loadChildrenById(id, lastId, pageLimit);
    }
}