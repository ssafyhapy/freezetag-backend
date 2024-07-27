package com.ssafy.freezetag.domain.oauth2.repository;
//
//
//import com.ssafy.freezetag.domain.oauth2.entity.Token;
//import java.util.Optional;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TokenRepository extends CrudRepository<Token, String> {
//
//    Optional<Token> findByAccessToken(String accessToken);
//}

import com.ssafy.freezetag.domain.oauth2.entity.Token;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    Optional<Token> findByRefreshToken(String refreshToken);
}
