package com.minh.jpa;

import com.minh.entity.Comment;
import com.minh.entity.ViewEmotion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ViewEmotionRepository extends JpaRepository<ViewEmotion, Integer> {
}