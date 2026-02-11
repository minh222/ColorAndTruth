package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RemoveCommentDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public RemoveCommentDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public Integer removeComment(Long id) {
        Integer countChild = r.commentRepository.getCountById(id);
        r.closureRepository.deleteById(id);
        r.commentRepository.deleteById(id);
        return countChild - 1;
    }
}