package com.trace.traceproject.domain;

import com.trace.traceproject.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNum;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "member_preference",
            joinColumns =@JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id"))
    private Set<Preference> preferences = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role",
            joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Member(String userId, String password, String name, String email, String phoneNum) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    /**
     * 회원 정보 변경
     */
    public void changeUserInfo(String phoneNum, Set<Preference> preferences){
        this.phoneNum = phoneNum;
        this.preferences = preferences;
    }

    /**
     * 비밀 번호 변경
     */
    public void changePassword(String password) {
        this.password = password;
    }

}

