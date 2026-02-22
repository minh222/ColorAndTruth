package com.minh.data.access.control.user;

import com.minh.config.DataAccess;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@DataAccess
public class UploadDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos rp;

    public UploadDataAccess(CurrentRepos repos) {
        this.rp = repos;
    }

    @Transactional
    public void updateAvatar(Long userId, String link) {
        User user = rp.userRp.getReferenceById(userId);
        user.setAvatar(link);
    }
}
