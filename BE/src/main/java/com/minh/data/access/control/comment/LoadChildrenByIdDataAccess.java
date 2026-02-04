package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.data.access.control.comment.response.LoadCommentResponse;
import com.minh.entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LoadChildrenByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public LoadChildrenByIdDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public List<LoadCommentResponse> loadChildrenById(Long id, Long lastId, int limit) {
        if (lastId == null) {
            Long maxId = repos.commentRepository.getMaxChildrenIdById(id);
            lastId = maxId == null ? null : maxId + 1;
        }
        Pageable pageLimit = PageRequest.of(0, limit);
        return repos.commentRepository.loadChildrenById(id, lastId, pageLimit);
    }


    @Transactional
    public Integer removeComment(Long id) {
        repos.commentRepository.deleteById(id);

        Long  parentId = repos.commentRepository.getParentId( id);
        List<Long> ids = Arrays.asList(parentId, id);
        repos.closureRepository.deleteByAncestorId(ids, id);
        return repos.commentRepository.getCountById(parentId);
    }
}