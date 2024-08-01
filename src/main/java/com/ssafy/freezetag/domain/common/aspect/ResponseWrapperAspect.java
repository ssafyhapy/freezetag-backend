package com.ssafy.freezetag.domain.common.aspect;

import com.ssafy.freezetag.domain.common.CommonResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseWrapperAspect {
    // 이름이 xxxController 인 경우
    @Around("execution(* com.ssafy.freezetag..*Controller.*(..))")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // controller 에서 반환한 DTO
        Object result = joinPoint.proceed();
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof CommonResponse) {
                // 이미 CommonResponse로 감싸진 경우
                return responseEntity;
            }

            return ResponseEntity.status(responseEntity.getStatusCode())
                    .body(CommonResponse.success(responseBody));
        }
        throw new IllegalStateException("controller 반환 값은 항상 ResponseEntity 를 사용해주세요");
    }

}
