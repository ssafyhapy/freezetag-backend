pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'sonjiseokk/freezetag'
        DOCKER_HUB_CREDENTIALS_ID = 'dockerhub2'
        NETWORK_NAME = 'my-network'
        GITLAB_CREDENTIALS_ID = 'gitlab' // GitLab 인증 정보 ID
        GITHUB_CREDENTIALS_ID = 'github-token'

        GITHUB_BACKEND_REPO_URL = "github.com/ssafyhapy/freezetag-backend.git"
        GITHUB_FRONTEND_REPO_URL = "github.com/ssafyhapy/freezetag-frontend.git"

        OPEN_AI_KEY = credentials('OPEN_AI_KEY') // OpenAI 키 추가
        S3_ACCESS_KEY = credentials('S3_ACCESS_KEY') // OpenAI 키 추가
        S3_SECRET_KEY = credentials('S3_SECRET_KEY') // OpenAI 키 추가
        RDS_URI = credentials('RDS_URI') // RDS URI
        RDS_PW = credentials('RDS_PW') // RDS PW

        MAIL_USERNAME = credentials('MAIL_USERNAME') // MAIL_USERNAME
        MAIL_PASSWORD = credentials('MAIL_PASSWORD') // MAIL_PASSWORD
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
                    def app = docker.build("${DOCKER_HUB_REPO}:latest",
                        "--build-arg OPEN_AI_KEY=${env.OPEN_AI_KEY} " +
                        "--build-arg S3_ACCESS_KEY=${env.S3_ACCESS_KEY} " +
                        "--build-arg S3_SECRET_KEY=${env.S3_SECRET_KEY} " +
                        "--build-arg RDS_URI=${env.RDS_URI} " +
                        "--build-arg RDS_PW=${env.RDS_PW} " +
                        "--build-arg MAIL_USERNAME=${env.MAIL_USERNAME} " +
                        "--build-arg MAIL_PASSWORD=${env.MAIL_PASSWORD} ."
                    )
                }
            }
        }



        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_HUB_CREDENTIALS_ID}") {
                        def app = docker.image("${DOCKER_HUB_REPO}:latest")
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
                                    docker pull ${DOCKER_HUB_REPO}:latest
                                    docker stop myapp || true
                                    docker rm myapp || true
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker stop
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker rm
                                    docker run -d --name myapp --network ${NETWORK_NAME} -p 8080:8080 ${DOCKER_HUB_REPO}:latest
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

        stage('Update GitLab Repository') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${GITLAB_CREDENTIALS_ID}", passwordVariable: 'GITLAB_PASSWORD', usernameVariable: 'GITLAB_USERNAME'),
                                 string(credentialsId: "${GITHUB_CREDENTIALS_ID}", variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git config --global user.email "thswltjr11@gmail.com"
                        git config --global user.name "sonjiseokk"

                        # Clone GitLab repository
                        git clone https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@lab.ssafy.com/s11-webmobile1-sub2/S11P12C209.git
                        cd S11P12C209

                        # Add backend subtree (to ensure it remains updated)
                        git subtree pull --prefix=backend https://${GITHUB_TOKEN}@${GITHUB_BACKEND_REPO_URL} main

                        # Add frontend subtree
                        git subtree pull --prefix=frontend https://${GITHUB_TOKEN}@${GITHUB_FRONTEND_REPO_URL} main

                        # Set remote URL for GitLab
                        git remote set-url origin https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@lab.ssafy.com/s11-webmobile1-sub2/S11P12C209.git

                        # Ensure there are changes to commit and force push
                        git add .
                        git commit -m "Update subtrees" || true
                        git push --force origin main
                    '''
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}