package com.minh.jpa;

import com.minh.data.access.control.comment.response.LoadCommentResponse;
import com.minh.entity.Closure;
import com.minh.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface ClosureRepository extends JpaRepository<Closure, Long> {
     List<Closure> findAllByDescendantId(Long id);

     Closure findByAncestorId(Long id);

     @Transactional
     @Modifying
     @Query("delete from Closure c where c.ancestorId in :ids and c.ancestorId <> : id ")
     void deleteByAncestorId(List<Long> ids, Long id);
}