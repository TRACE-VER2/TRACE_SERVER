package com.trace.traceproject.repository;

import com.trace.traceproject.dto.Token;
import com.trace.traceproject.security.jwt.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = {"test"})
@SpringBootTest
class TokenRedisRepositoryTest {

    @Autowired private TokenRedisRepository tokenRedisRepository;
    @Autowired private JwtUtil jwtUtil;

    @BeforeEach
    public void beforeEach() {
        Token token = new Token();
        String refreshToken = jwtUtil.generateRefreshToken("syleemk");
        token.setUsername("syleemk");
        token.setRefreshToken(refreshToken);
        token.setDuration(1);
        Token saveToken = tokenRedisRepository.save(token);
    }

    @AfterEach
    public void afterEach() {
        tokenRedisRepository.deleteAll();
    }

    @Test
    public void save() throws Exception {
        //given
        Token token = new Token();
        String refreshToken = jwtUtil.generateRefreshToken("dasog");
        token.setUsername("dasog");
        token.setRefreshToken(refreshToken);

        //when
        Token saveToken = tokenRedisRepository.save(token);

        //then
        Token findToken = tokenRedisRepository.findById("dasog").get();

        assertThat(findToken.getUsername()).isEqualTo("dasog");
        assertThat(findToken.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    public void update() throws Exception {
        //given
        String id = "syleemk";
        String refreshToken = "newToken";

        Token findToken = tokenRedisRepository.findById(id).get();

        //when
        findToken.setRefreshToken(refreshToken);
        tokenRedisRepository.save(findToken);

        //then
        Token changeToken = tokenRedisRepository.findById("syleemk").get();
        assertThat(changeToken.getUsername()).isEqualTo(id);
        assertThat(changeToken.getRefreshToken()).isEqualTo(refreshToken);

    }

    @Test
    public void delete() throws Exception {
        //given
        String id = "syleemk";
        Token token = tokenRedisRepository.findById(id).get();

        //when
        tokenRedisRepository.delete(token);

        //then
        Token findToken = tokenRedisRepository.findById(id).orElse(null);
        assertThat(findToken).isNull();
    }
}