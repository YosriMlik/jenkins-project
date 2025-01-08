pipeline {
    agent any

    tools {
        jdk 'JDK'
        maven 'Maven 3.9.9' // Ensure JDK8 is configured in Jenkins (Manage Jenkins > Global Tool Configuration)
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64' // Set Java home directory
        DOCKER_TAG = getVersion() // Generate Docker tag using Git commit hash
        //DOCKER_IMAGE = 'jmlhmd/image_name' // Replace with your Docker Hub username and image name
    }

    stages {
        stage ('Clone Stage') {
            steps {
                git branch: 'main', url: 'https://github.com/YosriMlik/jenkins-project.git' // Replace with your repo URL
            }
        }

        stage ('Build Maven Project') {
            steps {
                echo 'Building Maven project...'
                sh 'mvn clean package' // Build the Maven project
            }
        }

        stage ('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ." // Build Docker image
            }
        }

        stage ('DockerHub Push') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials', // Add Docker Hub credentials in Jenkins
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
                sshagent(credentials: ['Vagrant_ssh']) { // Add SSH credentials in Jenkins
                    sh """
                        ssh user@Ip_Recette 'sudo docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}' // Pull Docker image on remote server
                        ssh user@Ip_Recette 'sudo docker run -d -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}' // Run Docker container
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
    def version = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim() // Get Git commit hash
    return version
}