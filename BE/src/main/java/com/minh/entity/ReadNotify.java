package com.minh.entity;

import com.minh.entity.composite.id.ReadNotifyId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "read_notify")
@Getter
@Setter
@NoArgsConstructor
public class ReadNotify {


    @Id
    private ReadNotifyId id;
    
}
