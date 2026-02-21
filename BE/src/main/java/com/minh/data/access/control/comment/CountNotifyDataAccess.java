package com.minh.data.access.control.comment;

import com.minh.config.DataAccess;
import com.minh.config.Emitter;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Closure;
import com.minh.entity.Comment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.minh.config.Config.NOW;
import static com.minh.config.Config.TODAY;

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