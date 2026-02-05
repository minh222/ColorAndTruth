package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RemoveCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public RemoveCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public void removeComment(Long id) {
        r.closureRepository.deleteById(id);
        r.commentRepository.deleteById(id);
    }
}