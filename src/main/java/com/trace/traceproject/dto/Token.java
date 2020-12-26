package com.trace.traceproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@RedisHash(value = "token")
public class Token implements Serializable {//자바 직렬화 가능하려면 Serializable 마커 인터페이스 구현해야함
    //serialVersionUID를 설정해줘야 역직렬화시 오류 줄여줌
    private static final long serialVersionUID = -7353484588260422449L;
    @Id //key 값으로 설정
    private String username;
    private String refreshToken;
    @TimeToLive(unit = TimeUnit.HOURS) //redis 만료시간 100일 설정
    private Integer duration = 24 * 100;

    public Token(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }

    public Token(String username, String refreshToken, Integer duration) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.duration = duration;
    }
}
