package com.ssafy.freezetag.global.config;

import com.ssafy.freezetag.domain.oauth2.TokenExceptionFilter;
import com.ssafy.freezetag.domain.oauth2.handler.OAuth2SuccessHandler;
import com.ssafy.freezetag.domain.oauth2.service.CustomOAuth2UserService;
import com.ssafy.freezetag.domain.oauth2.TokenAuthenticationFilter;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenService tokenService; // 추가

    // TODO : 나중에 전역 Pattern 지우기
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/home", "/login", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile", "/public/**").permitAll() // added "/public/**"
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpoint ->
                                                userInfoEndpoint.userService(customOAuth2UserService)
                                        // 로그인 성공 시 핸들러
                                )
                                .successHandler(oAuth2SuccessHandler)
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // 로그아웃 요청 URL
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .addLogoutHandler(new LogoutHandler() { // 익명 클래스
                                    @Override
                                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                                        if (authentication != null) {
                                            String username = authentication.getName();
                                            tokenService.deleteRefreshToken(username); // Redis에서 토큰 삭제
                                        }
                                    }
                                })
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링
        ;

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
