pipeline {
    agent any
    
    tools{ 
        jdk 'JDK19' 
       

    }
    
    environment { 
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-19'
        M2 = 'D:\\apache-maven-3.9.9\\bin'
    }
    
    stages {
      stage ('clone repo') {
            steps {
               git branch: 'main', url: 'https://github.com/abdellahdhahri/jenkinsProject.git'
            }
        }
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'apache-maven-3.9.9') {
                    bat 'mvn clean compile'
                }
            }
        }
        
        stage ('Testing Stage') {
            steps {
                withMaven(maven : 'apache-maven-3.9.9') {
                    bat 'mvn test'
                } 
            }
        }
        
        stage ('Install Stage') {
            steps {
                withMaven(maven : 'apache-maven-3.9.9') {
                    bat 'mvn install'
                } 
            } 
        }
    }
}
