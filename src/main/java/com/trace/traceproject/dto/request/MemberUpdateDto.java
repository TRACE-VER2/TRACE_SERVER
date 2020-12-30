package com.trace.traceproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
//@NoArgsConstructor // 이거 있어야 serialize 오류 안일어남 -> 생성자 아무것도 없으면 기본 생성자 만들어줌
public class MemberUpdateDto {
    private String phoneNum;
    private List<String> preferences;
}
