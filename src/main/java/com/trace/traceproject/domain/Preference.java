package com.trace.traceproject.domain;

import com.trace.traceproject.domain.enums.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Preference extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "preference_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Tag tag;

    public Preference(Tag tag) {
        this.tag = tag;
    }
}
