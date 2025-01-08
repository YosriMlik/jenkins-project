pipeline {
    agent any
    
    tools{ 
        maven 'Maven 3.9.9'
    }
    
    /*environment { 
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-19'
        M2 = 'D:\\apache-maven-3.9.9\\bin'
    }*/
    
    stages {
      stage ('Clone Repo') {
            steps {
               git branch: 'main', url: 'https://github.com/YosriMlik/jenkins-project.git'
            }
        }
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'Maven 3.9.9') {
                    bat 'mvn clean compile'
                }
            }
        }
        
        stage ('Testing Stage') {
            steps {
                withMaven(maven : 'Maven 3.9.9') {
                    bat 'mvn test'
                } 
            }
        }
        
        stage ('Install Stage') {
            steps {
                withMaven(maven : 'Maven 3.9.9') {
                    bat 'mvn install'
                } 
            } 
        }
    }
}
