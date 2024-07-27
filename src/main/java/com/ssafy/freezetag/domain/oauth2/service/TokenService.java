package com.ssafy.freezetag.domain.oauth2.service;
//
//import com.ssafy.freezetag.domain.oauth2.TokenException;
//import com.ssafy.freezetag.domain.oauth2.entity.Token;
//import com.ssafy.freezetag.domain.oauth2.repository.TokenRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class TokenService {
//
//    private final TokenRepository tokenRepository;
//
//    public void deleteRefreshToken(String memberKey) {
//        tokenRepository.deleteById(memberKey);
//    }
//
//    @Transactional
//    public void saveOrUpdate(String memberKey, String refreshToken, String accessToken) {
//        Token token = tokenRepository.findByAccessToken(accessToken)
//                .map(o -> o.updateRefreshToken(refreshToken))
//                .orElseGet(() -> new Token(memberKey, refreshToken, accessToken));
//
//        tokenRepository.save(token);
//    }
//
//    public Token findByAccessTokenOrThrow(String accessToken) {
//        return tokenRepository.findByAccessToken(accessToken)
//                .orElseThrow(() -> new TokenException("TOKEN_EXPIRED"));
//    }
//
//    @Transactional
//    public void updateToken(String accessToken, Token token) {
//        token.updateAccessToken(accessToken);
//        tokenRepository.save(token);
//    }
//}

import com.ssafy.freezetag.domain.oauth2.TokenException;
import com.ssafy.freezetag.domain.oauth2.entity.Token;
import com.ssafy.freezetag.domain.oauth2.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(String memberKey) {
        tokenRepository.deleteById(memberKey);
    }

    @Transactional
    public void saveOrUpdate(String memberKey, String refreshToken) {
        Token token = tokenRepository.findById(memberKey)
                .map(existingToken -> existingToken.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(memberKey, refreshToken));

        tokenRepository.save(token);
    }

    public Token findByRefreshTokenOrThrow(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenException("TOKEN_EXPIRED"));
    }
}
