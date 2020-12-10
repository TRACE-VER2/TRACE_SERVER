package com.trace.traceproject.security.service;

import com.trace.traceproject.security.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //bean등록되어있는 인터페이스의 구현체를 주입해줌
    private final UserDbService userDbService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserInfo userInfo = userDbService.getUserInfo(userId);
        if(userInfo == null)
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        return CustomUserDetails.builder()
                .userId(userInfo.getUserId())
                .password(userInfo.getPassword())
                .email(userInfo.getEmail())
                .roles(userInfo.getRoles())
                .build();
    }
}
