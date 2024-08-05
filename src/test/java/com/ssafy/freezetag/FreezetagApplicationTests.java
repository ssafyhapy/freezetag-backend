package com.ssafy.freezetag;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "openai.key=${OPEN_AI_KEY}",
        "s3.access.key=${S3_ACCESS_KEY}",
        "s3.secret.key=${S3_SECRET_KEY}"
})
@Slf4j
public class FreezetagApplicationTests {
    @Value("${openai.key}")
    private String openAiKey;

    @Value("${s3.access.key}")
    private String s3AccessKey;

    @Value("${s3.secret.key}")
    private String s3SecretKey;

    @Test
    public void contextLoads() {
        // 환경 변수가 올바르게 주입되었는지 확인하는 테스트 코드
        System.out.println(openAiKey + " : " + s3AccessKey + " : " + s3SecretKey);
        assertNotNull(openAiKey);
        assertNotNull(s3AccessKey);
        assertNotNull(s3SecretKey);
    }
}


