package com.trace.traceproject.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberUpdateDto {
    private String userId;
    private String phoneNum;
    private List<String> preferences = new ArrayList<>();

    @Builder
    public MemberUpdateDto(String userId, String phoneNum) {
        this.userId = userId;
        this.phoneNum = phoneNum;
    }
}
