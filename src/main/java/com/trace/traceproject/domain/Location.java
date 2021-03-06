package com.trace.traceproject.domain;

import com.trace.traceproject.domain.enums.LocationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private LocationStatus name;

    //Test용 생성자
    public Location(LocationStatus name) {
        this.name = name;
    }
}