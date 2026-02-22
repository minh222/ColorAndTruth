package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.ReadNotify;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@DataAccess
public class ReadNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public ReadNotifyDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    @Transactional
    public void readNotify(Long userId ) {
        List<ReadNotify> notifies = new ArrayList<>();
        rp.commentRp.getReplyIds(userId).forEach(
                id -> notifies.add(new ReadNotify(userId, id)));
        rp.readNotifyRp.saveAll(notifies);
    }
}