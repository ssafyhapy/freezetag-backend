pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'sonjiseokk/freezetag'
        DOCKER_HUB_CREDENTIALS_ID = 'dockerhub2'
        NETWORK_NAME = 'my-network'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew build'
                sh 'ls -la build/libs/'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def app = docker.build("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}", ".")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_HUB_CREDENTIALS_ID}") {
                        def app = docker.image("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}")
                        app.push()
                    }
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'ubuntu', // Jenkins SSH 서버 설정 이름
                        transfers: [
                            sshTransfer(
                                sourceFiles: '', // 파일 전송이 필요 없으므로 빈 문자열
                                execCommand: """
                                    docker pull ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}
                                    docker stop myapp || true
                                    docker rm myapp || true
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker stop
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker rm
                                    docker run -d --name myapp --network ${NETWORK_NAME} -p 8080:8080 ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}

                                """,
                                remoteDirectory: '/home/ubuntu', // 원격 디렉토리
                                removePrefix: ''
                            )
                        ],
                        usePromotionTimestamp: false,
                        useWorkspaceInPromotion: false,
                        verbose: true
                    )
                ])
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}