pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'sonjiseokk/freezetag'
        BLUE_CONTAINER = 'backend-blue'
        GREEN_CONTAINER = 'backend-green'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    // Docker 이미지를 빌드
                    sh 'docker build -t ${DOCKER_IMAGE}:latest .'
                }
            }
        }
        stage('Push') {
            steps {
                script {
                    // Docker 이미지를 푸시
                    sh 'docker push ${DOCKER_IMAGE}:latest'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // 현재 사용 중인 컨테이너 확인
                    def current_container = sh(script: "docker ps --filter 'name=${BLUE_CONTAINER}' --format '{{.Names}}'", returnStdout: true).trim()
                    def new_container = (current_container == BLUE_CONTAINER) ? GREEN_CONTAINER : BLUE_CONTAINER

                    // 새로운 버전의 컨테이너 실행
                    sh "docker-compose up -d --no-deps --scale ${new_container}=1 ${new_container}"

                    // 기존 컨테이너 중지
                    sh "docker-compose stop ${current_container}"

                    // 사용하지 않는 컨테이너 제거
                    sh "docker-compose rm -f ${current_container}"
                }
            }
        }
    }
}
