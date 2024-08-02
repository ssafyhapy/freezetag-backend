package com.ssafy.freezetag.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.freezetag.domain.common.CommonResponse;
import com.ssafy.freezetag.domain.exception.custom.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            handleTokenException(response, e);
        }
    }

    private void handleTokenException(HttpServletResponse response, TokenException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
        response.setContentType("application/json");
        String jsonResponse = objectMapper.writeValueAsString(CommonResponse.failure(e.getMessage()));
        response.getWriter().write(jsonResponse);
    }

}
