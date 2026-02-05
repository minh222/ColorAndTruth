package com.minh.data.access.control.user;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minh.config.Exception.http;

@Service
public class EmptyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public EmptyDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public void emptyAvatar(Long userId) {
        User user =  r.userRepository.findById(userId);
        user.resetCountToday();

        if (user.exceed()) {
            throw http(429, "Mỗi ngày chỉ được xóa avatar 20 lần");
        }

        user.emptyAvatarAndIncreaseCounter();
    }
}
