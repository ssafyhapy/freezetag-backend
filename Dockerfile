# 기본 이미지를 설정합니다. 일반적으로 OpenJDK를 사용합니다.
FROM openjdk:17-slim

# 애플리케이션 jar 파일을 복사할 디렉토리를 생성합니다.
VOLUME /tmp

# 빌드된 jar 파일을 Docker 이미지에 복사합니다.
ARG JAR_FILE=build/libs/devops-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java","-jar","/app.jar"]
