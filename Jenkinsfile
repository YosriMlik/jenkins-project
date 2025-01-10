pipeline {
    agent any

    tools {
        jdk 'JDK'
        maven 'Maven 3.9.9'
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        DOCKER_TAG = "latest"
        DOCKER_IMAGE = 'yosrimlik/image_name' // Replace with your Docker Hub username and image name
    }

    stages {
        stage ('Clone Stage') {
            steps {
                git branch: 'main', url: 'https://github.com/YosriMlik/jenkins-project.git' 
            }
        }

        stage ('Build Maven Project') {
            steps {
                echo 'Building Maven project...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage ('Cleanup Old Containers') {
            steps {
                echo 'Cleaning up old Docker containers...'
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'vm2-ssh', // Use the ID of the SSH credentials
                    keyFileVariable: 'SSH_KEY'
                )]) {
                    sh """
                        # Stop running containers with the same image name
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ubuntu-server@192.168.11.132 "docker stop $(docker ps -q --filter ancestor=yosrimlik/image_name:latest)"
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ubuntu-server@192.168.11.132 "docker rm $(docker ps -aq --filter ancestor=yosrimlik/image_name:latest)"

                        # Remove all containers (running or stopped) with the same image name
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ubuntu-server@192.168.11.132 "docker rmi -f yosrimlik/image_name:latest"
                    """
                }
            }
        }

        stage ('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }

        stage ('DockerHub Push') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials', // Use the ID of the credentials you added
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin" // Log in to Docker Hub
                }
                sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}" // Push Docker image
            }
        }

        stage ('Deploying to VM2') {
            steps {
                echo 'Deploying Docker container to VM2...'
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'vm2-ssh', // Use the ID of the SSH credentials
                    keyFileVariable: 'SSH_KEY'
                )]) {
                    sh """
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ubuntu-server@192.168.11.132 "docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}"
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ubuntu-server@192.168.11.132 "docker run -d -p 8081:8081 ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

// Custom function to generate Docker tag
def getVersion() {
    def version = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
    return version
}