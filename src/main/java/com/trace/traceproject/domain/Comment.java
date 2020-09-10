package com.trace.traceproject.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @Lob@Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @ManyToOne
    private User user;

    @ManyToOne
    private House house;

    //별점
    private int starGrade;

}
