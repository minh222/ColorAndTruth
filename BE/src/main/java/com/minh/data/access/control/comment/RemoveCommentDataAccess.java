package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RemoveCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public RemoveCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public Integer removeComment(Long id) {
        List<Long> descendantIds = r.closureRepository.getDescendantId(id);
        Integer count = r.closureRepository.getNumChild(id) - r.commentRepository.deleteAllByIdIn(descendantIds);
        r.closureRepository.deleteByIdIn(descendantIds);
        return count;
    }
}