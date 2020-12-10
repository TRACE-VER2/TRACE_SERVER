package com.trace.traceproject.security.service;

import com.trace.traceproject.security.dto.UserInfo;

import java.util.Optional;

//spring security와 domain을 연결해주는 역할
public interface UserDbService {
    UserInfo getUserInfo(String userId);
}
