package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.controller.notify.response.NotifyResponse;
import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@DataAccess
public class GetNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public GetNotifyDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<NotifyResponse> getNotify(Long id) {
        return r.commentRepository.getNotify(id);
    }
}