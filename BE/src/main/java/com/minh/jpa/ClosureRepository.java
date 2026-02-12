package com.minh.jpa;

import com.minh.entity.Closure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ClosureRepository extends JpaRepository<Closure, Long> {
     List<Closure> findAllByDescendantId(Long id);


     @Transactional
     @Modifying
     @Query("delete from Closure c where c.descendantId = :id or c.ancestorId = :id ")
     void deleteById(Long id);

     @Query("select  c.descendantId  from Closure c where c.ancestorId = :id  ")
     List<Long> getDescendantId(Long id);

     @Transactional
     @Modifying
     @Query("delete from Closure c where c.descendantId in :descendantIds or c.ancestorId in :descendantIds ")
     void deleteByIdIn(List<Long> descendantIds);

     @Query( "select count(c.id) from Closure c " +
             "where c.ancestorId = (select  x.parentId  from Comment x where x.id = :id) " +
             "and  c.ancestorId <>  c.descendantId ")
     Integer getNumChild(Long id);
}