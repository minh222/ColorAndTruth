package com.minh.jpa;

import com.minh.entity.Closure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ClosureRepository extends JpaRepository<Closure, Long> {
     List<Closure> findAllByDescendantId(Long id);

     Closure findByAncestorId(Long id);

     @Transactional
     @Modifying
     @Query("delete from Closure c where c.descendantId = :id or c.ancestorId = :id ")
     void deleteById(Long id);
}