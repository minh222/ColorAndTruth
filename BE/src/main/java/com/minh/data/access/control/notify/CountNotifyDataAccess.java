package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

@Service
@DataAccess
public class CountNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public CountNotifyDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public Integer getBadge(Long userId) {
        return r.commentRepository.getBadge(userId);
    }
}