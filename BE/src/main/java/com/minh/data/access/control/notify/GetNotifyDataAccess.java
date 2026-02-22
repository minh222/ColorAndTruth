package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.controller.notify.response.NotifyResponse;
import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DataAccess
public class GetNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public GetNotifyDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public List<NotifyResponse> getNotify(Long userId) {
        List<Long> replyIds = rp.readNotifyRp.getReplyIds(userId);
        return rp.commentRp.getNotify(userId, nullIfEmpty(replyIds));
    }

    private List<Long> nullIfEmpty(List<Long> list) {
        return list.isEmpty() ? null : list;
    }
}