pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the image using the Dockerfile which copies the source code
                    bat 'docker build -t demowebshop-tests -f .devcontainer/Dockerfile.dev .'
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    bat 'docker rm -f runner || exit 0'
                    
                    def exitCode = bat(
                        returnStatus: true,
                        script: 'docker run --name runner demowebshop-tests mvn clean test allure:report'
                    )
                    
                    if (exitCode != 0) {
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('Extract Reports') {
            steps {
                script {
                    bat 'if not exist target mkdir target'
                    
                    // Correct paths: container uses WORKDIR /workspace
                    bat 'docker cp runner:/workspace/target/surefire-reports ./target/surefire-reports || exit 0'
                    bat 'docker cp runner:/workspace/target/cucumber-reports ./target/cucumber-reports || exit 0'
                    bat 'docker cp runner:/workspace/target/allure-results ./target/allure-results || exit 0'
                    
                    bat 'docker rm -f runner'
                }
            }
        }
    }
    
    post {
        always {
            // Publish JUnit test results
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            
            // Archive Cucumber reports
            archiveArtifacts artifacts: 'target/cucumber-reports/**/*', allowEmptyArchive: true
            
            // Publish Allure report
            allure([
                includeProperties: false,
                jdk: '',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}

