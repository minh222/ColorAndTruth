package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.controller.notify.response.NotifyResponse;
import com.minh.data.access.control.CurrentRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DataAccess
public class ReadNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public ReadNotifyDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    public List<NotifyResponse> getReadNotify(Long userId) {
        List<Long> replyIds = rp.readNotifyRp.getReplyIds(userId);
        return rp.commentRp.getReadNotify(userId, nullIfEmpty(replyIds));
    }

    private List<Long> nullIfEmpty(List<Long> list) {
        return list.isEmpty() ? null : list;
    }
}