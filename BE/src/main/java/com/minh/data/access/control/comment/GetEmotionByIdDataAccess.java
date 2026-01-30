package com.minh.data.access.control.comment;

import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

@Service
public class GetEmotionByIdDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos repos;

    public GetEmotionByIdDataAccess(CurrentRepos repos) {
        this.repos = repos;
    }

    public String getEmotionById(Long lastId) {
        return repos.commentRepository.getEmotionById(lastId);
    }
}