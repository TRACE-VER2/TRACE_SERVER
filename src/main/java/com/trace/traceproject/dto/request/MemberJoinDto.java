package com.trace.traceproject.dto.request;

import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class MemberJoinDto {
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phoneNum;
    private List<String> preferences = new ArrayList<>();

    public Member toEntity(){
        return Member.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .name(name)
                .phoneNum(phoneNum)
                .build();
    }

    public MemberJoinDto(String userId, String password, String email, String name, String phoneNum) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
    }
}
