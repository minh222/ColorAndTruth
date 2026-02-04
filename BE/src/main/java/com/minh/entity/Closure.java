package com.minh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "closure")
@Getter
@Setter
public class Closure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long ancestorId;

    @Column
    private Long descendantId;



    public Closure( Long ancestorId, Long descendantId ) {
        this.ancestorId = ancestorId;
        this.descendantId = descendantId;

    }

    public Closure() {

    }


}
