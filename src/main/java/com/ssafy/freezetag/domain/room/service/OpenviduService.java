package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import com.ssafy.freezetag.domain.exception.custom.OpenviduTokenException;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
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

    public OpenviduResponseDto createRoom() throws OpenviduTokenException {
        try {
            // 세션 생성
            SessionProperties properties = new SessionProperties.Builder().build();
            Session session = openvidu.createSession(properties);
            log.info("Session.getSessionId() = {}", session.getSessionId());
            // 연결 속성 설정
            ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .build();
            // 토큰 생성
            String openviduToken = session.createConnection(connectionProperties).getToken();
            log.info("OpenViduToken = {}", openviduToken);
            // 세션 ID와 토큰을 DTO 에 저장
            return new OpenviduResponseDto(session.getSessionId(), openviduToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.fillInStackTrace();
            throw new OpenviduTokenException();
        }
    }

    public OpenviduResponseDto enterRoom(String sessionId) throws OpenviduTokenException {
        try {
            // 세션 ID로 세션 접속
            Session session = openvidu.getActiveSession(sessionId);

            // 연결 속성 설정
            ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .build();

            // 토큰 생성
            String openviduToken = session.createConnection(connectionProperties).getToken();

            // 세션 ID와 토큰을 DTO 에 저장
            return new OpenviduResponseDto(session.getSessionId(), openviduToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.fillInStackTrace();
            throw new OpenviduTokenException();
        }
    }
}
