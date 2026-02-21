package com.minh.jpa;

import com.minh.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {


    @Query("select n.fromId from  Notify n where  n.toId =  :toId")
    List<Long> getFromIdByToId(Long toId);
}