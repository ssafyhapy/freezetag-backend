# 🧊 사르르


![메인페이지](https://github.com/user-attachments/assets/db6ec033-055f-4520-9414-09c8921afdbf)


# 목차
1. [서비스 소개](#-서비스-소개)
2. [화면 소개](#-화면-소개)
3. [기술 스택](#-기술-스택)
4. [서비스 아키텍처](#-서비스-아키텍처)
5. [프로젝트 산출물](#-프로젝트-산출물)

# ✨ 서비스 소개
### 기획 배경
**AS-IS**
- 오프라인에 한정 : 온라인으로 만나는 모임은 점점 증가 하지만 친해질 수 있는 통로 부재
- 진행자의 부담 : 진행자가 게임을 모두 기획, 준비하며 아이스브레이킹 진행 부담

**TO-BE**
- 언제 어디서나 인터넷만 있다면
- 다양한 컨텐츠 활용 가능
- 대화 내용 기록 및 보관
- 다양한 상호작용

### 타켓
- 어색한 분위기 속 아이스크레이킹이 필요항 사람들
- 물리적인 이유로 사람들을 만나지 못하는 사람들

### 주요 기능
1. WebRTC를 활용해 비대면으로 아이스 브레이킹 게임을 즐길 수 있는 환경 제공
2. 한 줄 자기소개, 나를 맞춰봐(OX게임), 밸런스게임 등 서로를 충분히 알아갈 수 있는 게임 요소 제공
3. 게임 결과를 기록하여 추억을 돌아볼 수 있는 결과 레포트 제공

# 💻 화면 소개
![슬라이드1](https://github.com/user-attachments/assets/c96bc0e0-7cc8-42b7-83fa-5aea529b1ca3)
![슬라이드2](https://github.com/user-attachments/assets/0865c3a4-be6a-45b5-a3e1-ff04cac9735c)
![메인페이지](https://github.com/user-attachments/assets/2688f44c-06f5-48a3-9e17-114dbe625071)
![슬라이드3](https://github.com/user-attachments/assets/8ec47129-28f9-46a3-97e0-f976953469dd)
![게임소개](https://github.com/user-attachments/assets/8548df2e-d8d2-4977-b0b9-58c11da62f99)
![슬라이드4](https://github.com/user-attachments/assets/7c29a6b6-eb40-4a51-bae9-40a1d74039ae)
![슬라이드5](https://github.com/user-attachments/assets/17a54549-45f9-4147-aaf8-361ad5898f45)
![슬라이드6](https://github.com/user-attachments/assets/990b450a-9351-404d-b6d9-f4aa8d357e6e)
![슬라이드8](https://github.com/user-attachments/assets/23e1f15c-99d5-4c24-98ca-32514c20b132)
![슬라이드9](https://github.com/user-attachments/assets/84308a1f-cfd2-4565-acae-3b857d7d6d6c)
![슬라이드10](https://github.com/user-attachments/assets/49f06fe4-1efb-44d2-b38c-e967882c4532)
![슬라이드11](https://github.com/user-attachments/assets/8ad4f9cf-2f38-4385-8b05-e41b6b672dbe)
![슬라이드12](https://github.com/user-attachments/assets/0dbebe8b-06af-427e-9992-f9488f90aac4)
![슬라이드13](https://github.com/user-attachments/assets/75f1a73f-8824-4bf2-8963-93d98a07a49c)
![슬라이드14](https://github.com/user-attachments/assets/b3c57148-6650-45c7-a69d-96e807144ba2)
![슬라이드15](https://github.com/user-attachments/assets/30c5d6ca-c611-44d8-a822-be9ffa66b580)
![슬라이드16](https://github.com/user-attachments/assets/8584c14c-8eb1-4f78-9c33-4d48888be42c)
![슬라이드17](https://github.com/user-attachments/assets/db8b31f2-a15d-4a1e-a1eb-4aa10a56886b)
![슬라이드18](https://github.com/user-attachments/assets/adb56a43-8b1b-43f7-ba71-d43b9aa2ef55)
![슬라이드19](https://github.com/user-attachments/assets/ace187da-4299-4dc2-9d79-c500ab216b3d)
![슬라이드20](https://github.com/user-attachments/assets/6bd82e1b-c339-4cc3-bc34-cdda71b5a529)
![슬라이드21](https://github.com/user-attachments/assets/97111238-999d-4eeb-ab7e-7b0070570209)
![슬라이드24](https://github.com/user-attachments/assets/77ee52f8-cdc7-41c7-b9fe-795abd577aff)
![슬라이드25](https://github.com/user-attachments/assets/4db94483-053d-45ee-9a0c-46c75061ef77)
![슬라이드26](https://github.com/user-attachments/assets/c5e16305-9efc-4195-b3f7-13d49bc9ed12)
![슬라이드28](https://github.com/user-attachments/assets/7dd32c32-f1bb-4c2e-aeac-1516349a43e7)
![슬라이드29](https://github.com/user-attachments/assets/822148ae-b095-4573-a748-578f6101125f)
![슬라이드32](https://github.com/user-attachments/assets/59cb0541-4c36-4e9e-827e-ac3de469c796)


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
  - `"/api/pub/message/{roomId}"`
  - `"/api/sub/message/{roomId}"`
- **게임에 존재하는 4단계의 게임 중 다음 단계로 넘기기**
  - `"/api/pub/{roomId}/state"`
  - `"/api/sub/{roomId}/state"`
- **작성 및 전체 유저 작성 여부 확인**
  - `"/api/pub/{게임이름}/{roomId}/check"`
  - `"/api/sub/{게임이름}/{roomId}/check"`
- **하나의 게임내에서 다음 유저 차례 넘기기**
  - `"/api/pub/{게임이름}/{roomId}/next"`
  - `"/api/sub/{게임이름}/{roomId}/next"`
- **유저 화상화면 뒷배경 색상 변경**
  - `"/api/pub/{게임이름}/{roomId}/selection"`
  - `"/api/sub/{게임이름}/{roomId}/selection"`

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
![모션인식결과](https://github.com/user-attachments/assets/cde27727-1f32-4b86-9398-7972800e05ed)

### 모션인식
- 실시간 영상에서 O,X 모션을 인식
- `O, X, N` 3-Class classification task
- 약 10,000여개의 이미지 데이터를 활용해 이미지 분류
- Teachable Machine을 활용하여 별도의 GPU 서버를 활용하지 않고 효율적으로 훈련

# 🎨 서비스 아키텍처

![](https://i.imgur.com/yinPQjZ.png)


- 사용자의 요청이 `nginx`의 `reverse proxy`를 이용하여 라우팅 됨.
  - `/` 주소에 대해서 frontend page로 라우팅
  - `/api` 주소에 대해서 backend api 요청`주황색` 라인에 대해서 `gitlab-runner`를 이용하여 자동 배포를 위한 `cicd` 구축
  - `openvidu` 는 backend에서 사용자 인증 후에 `8443` 포트의 openvidu backend server에서 `token` 반환
- `Jenkinsfile` 을 통한 깃허브, 깃랩 자동 CI/CD 구현

# 📚 프로젝트 산출물
## 프로젝트 진행
## 1. Git Flow
![](https://i.imgur.com/8FQQNd9.gif)


## 프로젝트 산출물
## 1. Figma(https://ko.fm/7HO)
![사르르-프로토타입](https://github.com/user-attachments/assets/4d057112-9fc5-4685-bb22-89094c2c223f)

## 2. ERD
![ERD](https://github.com/user-attachments/assets/a4abc0a0-c634-49ad-bb9f-0ac908a22fc4)

## 3. 요구사항 명세서
![요구사항명세서](https://github.com/user-attachments/assets/ab43c099-70f7-4f5b-bcd5-f5465ed09d0f)
