pipeline {
    agent any

    tools {
        jdk 'JDK'
        maven 'Maven 3.9.9'
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        DOCKER_TAG = getVersion()
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
                sh 'mvn clean package'
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

        stage ('Deploy') {
            steps {
                echo 'Deploying Docker container to remote server...'
                sshagent(credentials: ['Vagrant_ssh']) {
                    sh """
                        ssh user@Ip_Recette 'sudo docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}'
                        ssh user@Ip_Recette 'sudo docker run -d -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}'
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