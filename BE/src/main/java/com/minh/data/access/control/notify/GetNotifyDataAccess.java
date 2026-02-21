package com.minh.data.access.control.notify;

import com.minh.config.DataAccess;
import com.minh.controller.notify.response.NotifyResponse;
import com.minh.data.access.control.CurrentRepos;
import com.minh.entity.Comment;
import com.minh.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@DataAccess
public class GetNotifyDataAccess { // gateway :mỗi bussiness truy cập 1 cổng.
    public final CurrentRepos r;

    public GetNotifyDataAccess(CurrentRepos repos) {
        this.r = repos;
    }

    public List<NotifyResponse> getNotify(Long id) {
        List<Comment> comments = r.commentRepository.getUserIdByUserId(id);

        List<NotifyResponse> responses = new ArrayList<>();
        comments.forEach(c -> {
            Comment comment = r.commentRepository.getReferenceById(c.getParentId());
            User user = r.userRepository.getReferenceById(c.getUserId());
            responses.add(new NotifyResponse(comment.getClaim(), c.getClaim(), user.getName(), user.getAvatar()));
        });

        return responses;
    }

}