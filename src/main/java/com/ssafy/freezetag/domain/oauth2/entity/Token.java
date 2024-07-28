package com.ssafy.freezetag.domain.oauth2.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/*
    유저의 고유 id를 기준으로 refreshToken mapping
 */
@Getter
@AllArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 7)
public class Token {

    @Id
    private String id;

    private String refreshToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}