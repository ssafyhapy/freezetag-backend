package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.room.entity.dto.OpenviduDto;
import com.ssafy.freezetag.global.exception.OpenviduTokenException;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OpenviduService {
    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    public OpenviduDto createRoom() throws OpenviduTokenException {
        try {
            // 세션 생성
            SessionProperties properties = new SessionProperties.Builder().build();
            Session session = openvidu.createSession(properties);

            // 연결 속성 설정
            ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .build();

            // 토큰 생성
            String openviduToken = session.createConnection(connectionProperties).getToken();

            // 세션 ID와 토큰을 DTO 에 저장
            return OpenviduDto.builder()
                    .token(openviduToken)
                    .sessionId(session.getSessionId())
                    .build();
        } catch (Exception e) {
            throw new OpenviduTokenException();
        }
    }
}
