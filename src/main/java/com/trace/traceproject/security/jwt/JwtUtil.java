package com.trace.traceproject.security.jwt;

import com.trace.traceproject.security.service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Jwt는 자가 수용적인 토큰
 * 매 인증마다 UserDetailsService를 참조하는 것은 맞지 않음
 * 기존 UsernamePasswordAuthentication 로직에 맞추어 인증하지 않고 db의존성 제거
 * Jwt 자체정보를 통해 인증하도록 변경
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 1000L * 2 * 60 * 60; //2시간
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 1000L * 24 * 60 * 60 * 7; //일주일


    /**
     * Jwt 자체 정보로 인증 토큰 생성
     */
    public Authentication getAuthentication(String token) {
        //Jwt에서 정보 추출
        Map<String, Object> parseInfo = getUserParseInfo(token);
        System.out.println("parseinfo: " + parseInfo);
        List<String> roles =(List)parseInfo.get("role");

        //User은 Userdetails 구현체
        UserDetails userDetails = User.builder()
                .username(String.valueOf(parseInfo.get("username")))
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()))
                .password("asd")
                .build();

        //Jwt자체 정보 바탕으로 db 참조 없이 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }


    /**
     * accessToken 발급
     */
    public String generateAccessToken(String username, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) //데이터
                .setIssuedAt(now) //토큰 발행일자
                .setExpiration(new Date(now.getTime() + JWT_ACCESS_TOKEN_VALIDITY)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * refreshToken 발급
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * jwt parsing 메서드
     */
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    public Map<String, Object> getUserParseInfo(String token) {
        Claims parseInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Map<String, Object> result = new HashMap<>();
        result.put("username", parseInfo.getSubject());
        result.put("role", parseInfo.get("role", List.class));
        return result;
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //validate token
    public Boolean validateToken(String token) {
        try{
            boolean result = !isTokenExpired(token);
            return result;
        } catch (Exception e){
            return false;
        }
    }

}
