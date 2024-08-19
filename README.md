# 🧊 사르르


![메인페이지](https://github.com/user-attachments/assets/db6ec033-055f-4520-9414-09c8921afdbf)


# 목차
1. 서비스 소개
2. 화면 소개
3. 기술 스택

# ✨ 서비스 소개
### 기획 배경
### 타켓
### 주요 기능


# 💻 화면 소개

# 🛠 기술 스택
## 1. WebRTC

### WebRTC란?
> WebRTC (Web Real-Time Communication)는 웹 브라우저 간에 플러그인의 도움 없이 서로 통신할 수 있도록 설계된 API 입니다. 음성 통화, 영상 통화, P2P 파일 공유 등으로 활용될 수 있습니다.

### openvidu
> 무료 오픈 소스 멀티 플랫폼 화상 회의 솔루션으로, 모든 하위 수준의 작업들을 래핑함으로써 사용자로 하여금 WebRTC를 보다 간단하게 적용할 수 있게 합니다. Zooting은 Openvidu를 통해 화상 시스템을 구축합니다.

## 2. 실시간 양방향 통신

### Web Socket
![websocket](https://github.com/user-attachments/assets/4c19a8a1-ca58-47b9-844b-740d10c70aac)
> Websocket이란 ws 프로토콜을 기반으로 클라이언트와 서버 사이에 지속적인 양방향 연결 스트림을 만들어주는 기술입니다. 이는 stateless한 성질을 가지는 HTTP 일부 통신의 한계를 극복해 주는 것으로 서버는 클라이언트에 데이터를 실시간으로 전달할 수 있게 됩니다.

### STOMP Protocol
![pubsub](https://github.com/user-attachments/assets/53511229-bcb0-4804-b859-ffc9d994cc6a)
 > STOMP(Simple/Streaming Text Oriented Messaging Protocol)는 텍스트 기반의 메시징 프로토콜로, 주로 메시지 브로커와 상호작용하기 위해 설계되었습니다. 주로 비동기 메시지 전달에 사용되며, 메시지를 주고받는 시스템에서 클라이언트가 메시지 브로커에 연결하여 메시지를 보내거나(pub) 구독(sub)할 수 있게 해줍니다.
 > 
 > 이때 실시간 양방향 통신을 위해 STOMP는 WebSocket 위에서(연결 상태) 동작하는 것이 일반적입니다. 
 
### 적용

- **다른 유저들과 실시간 채팅**
  - "/api/pub/message/{roomId}"
  - "/api/sub/message/{roomId}"
- **게임에 존재하는 4단계의 게임 중 다음 단계로 넘기기**
  - "/api/pub/{roomId}/state"
  - "/api/sub/{roomId}/state"
- **작성 및 전체 유저 작성 여부 확인**
  - "/api/pub/{게임이름}/{roomId}/check"
  - "/api/sub/{게임이름}/{roomId}/check"
- **하나의 게임내에서 다음 유저 차례 넘기기**
  - "/api/pub/{게임이름}/{roomId}/next"
  - "/api/sub/{게임이름}/{roomId}/next"
- **유저 화상화면 뒷배경 색상 변경**
  - "/api/pub/{게임이름}/{roomId}/selection"
  - "/api/sub/{게임이름}/{roomId}/selection"

## 3. Redis
![redis](https://github.com/user-attachments/assets/c4ff132f-7092-4581-b6fc-e2f5bcbfa636)
> Redis는 주로 애플리케이션 캐시나 빠른 응답 속도를 가진 데이터베이스로 사용되는 오픈 소스 In-Memory NoSQL 저장소 입니다.

### 적용
- **JWT Token을 사용한 로그인**
  - 로그인시 사용되는 Refresh-token을 저장하기 위해서 사용했습니다. 
  - 보안성을 강화하기 위해서 엑세스 토큰 갱신시에 리프레시 토큰도 같이 갱신되는 `Refresh Token Rotation` 방식을 채용하였습니다.
- **채팅 기록**
  - 게임 내에서 작성된 채팅 기록은 Redis에 저장됩니다. 
  - 만약 늦게 접속한 유저가 있다면, Redis에서 이전 채팅 내역을 조회할 수 있습니다. 이를 통해 모든 사용자가 실시간으로 채팅 기록을 확인할 수 있습니다.
- **게임 데이터**
  - 게임 진행 중 유저가 생성한 데이터는 Redis에 저장 및 조회됩니다. 
  - DB에 직접 저장할 경우, 모든 유저의 데이터 작성 여부를 조회하기 위한 잦은 DB 접근으로 인해 성능 저하가 발생할 수 있습니다. 
  - 이를 방지하기 위해 게임 도중에는 Redis에서 데이터를 관리하며, 게임 종료 후 Redis에서 DB로 데이터를 옮기는 로직이 구현되어 있습니다.

## 4. Java Mail Sender + Quartz Scheduler

![](https://i.imgur.com/ym2yMhq.png)

> Java Mail Sender는 Java 애플리케이션에서 이메일을 전송하기 위한 라이브러리로, 주로 Spring Framework와 함께 사용됩니다. 이 라이브러리는 이메일 메시지를 생성하고, SMTP 서버를 통해 전송할 수 있는 기능을 제공합니다.
>
> Quartz는 주로 Java 애플리케이션에서 작업 스케줄링을 위한 작업 예약 및 관리에 사용되는 라이브러리입니다.
>
> 사르르는 Quartz와 Java Mail Sender를 활용하여, 사용자가 게임을 시작한 지 30일이 지난 시점에 자동으로 게임의 레포트를 이메일로 발송하는 기능을 제공합니다. 이를 통해 사용자는 게임 플레이 통계를 포함한 상세한 레포트를 보며 해당 게임의 추억을 회상할 수 있습니다.

### 적용

- 사용자가 게임을 시작한 지 30일이 지난 시점에 자동으로 게임의 레포트를 이메일로 발송하는 기능을 제공
- 30일이 지난 시점을 판단하기 위해 Quartz 라이브러리를 사용

## 5. Teachable Machine
![모션인식학습](https://github.com/user-attachments/assets/c36069b2-727b-449c-9b21-f9c55a18e71d)


![모션인식결과](https://github.com/user-attachments/assets/cde27727-1f32-4b86-9398-7972800e05ed)

### 모션인식
- 실시간 영상에서 O,X 모션을 인식
- `O, X, N` 3-Class classification task
- 약 10,000여개의 이미지 데이터를 활용해 이미지 분류
- Teachable Machine을 활용하여 별도의 GPU 서버를 활용하지 않고 효율적으로 훈련

# 🐴 서비스 아키텍처

![](https://i.imgur.com/yinPQjZ.png)


- 사용자의 요청이 `nginx`의 `reverse proxy`를 이용하여 라우팅 됨.
  - `/` 주소에 대해서 frontend page로 라우팅
  - `/api` 주소에 대해서 backend api 요청`주황색` 라인에 대해서 `gitlab-runner`를 이용하여 자동 배포를 위한 `cicd` 구축
  - `openvidu` 는 backend에서 사용자 인증 후에 `8443` 포트의 openvidu backend server에서 `token` 반환
- `Jenkinsfile` 을 통한 깃허브, 깃랩 자동 CI/CD 구현
