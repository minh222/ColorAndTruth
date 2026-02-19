package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@DataAccess
public class RemoveCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public RemoveCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public void removeComment(Long id) {
        List<Long> descendantIds = r.closureRepository.getDescendantId(id);
        r.commentRepository.deleteAllByIdIn(descendantIds);
        r.closureRepository.deleteByIdIn(descendantIds);
    }
}