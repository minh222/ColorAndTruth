package com.minh.data.access.control.user;

import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UploadDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public UploadDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    @Transactional
    public void updateAvatar(Long userId, String link) {
        User user = r.userRepository.getReferenceById(userId);
        user.setAvatar(link);
    }
}
