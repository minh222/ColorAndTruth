package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadChildrenByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public LoadChildrenByIdDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public List<Comment> loadChildrenById(Long id, Long lastId, int limit) {
        if (lastId == null) {
            Long maxId = repos.commentRepository.getMaxChildrenIdById(id);
            lastId = maxId == null ? null : maxId + 1;
        }
        Pageable pageLimit = PageRequest.of(0, limit);
        return repos.commentRepository.loadChildrenById(id, lastId, pageLimit);
    }
}