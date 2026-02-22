package com.minh.data.access.control.user;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minh.config.Exception.http;

@Service
@DataAccess
public class EmptyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public EmptyDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    @Transactional
    public void emptyAvatar(Long userId) {
        User user =  rp.userRp.getReferenceById(userId);
        user.resetCountToday();

        if (user.exceed()) {
            throw http(429, "Mỗi ngày chỉ được xóa avatar 20 lần");
        }

        user.emptyAvatarAndIncreaseCounter();
    }
}
