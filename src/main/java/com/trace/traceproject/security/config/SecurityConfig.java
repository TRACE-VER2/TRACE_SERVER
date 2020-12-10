package com.trace.traceproject.security.config;

import com.trace.traceproject.security.jwt.JwtAuthenticationFilter;
import com.trace.traceproject.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    //암호화에 필요한 PasswordEncoder를 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //authenticationManger를 Bean등록
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //rest api이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼으로 리다이렉트 됨
                .csrf().disable() //rest api이므로 csrf보안이 필요없으므로 diable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt 토큰기반 인증이므로 세션 필요없기에 생성안함
                .and()
                    .authorizeRequests() //요청에 대한 사용권한 체크
                        .antMatchers("/api/v1/members/login", "/api/v1/members/join").permitAll() //가입 및 인증 주소는 누구나 접근 가능
                        .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
                    .antMatchers("/api/v1/images/**").hasRole("ADMIN") //접근권한 테스트용 설정
                    .anyRequest().hasRole("USER") //나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
