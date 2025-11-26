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
                    // Remove any existing container with the same name
                    bat 'docker rm -f runner || exit 0'
                    
                    // Run tests inside the container
                    // We use 'returnStatus: true' to prevent the pipeline from stopping immediately if tests fail
                    // This allows us to extract reports even after failure
                    def exitCode = bat(returnStatus: true, script: 'docker run --name runner demowebshop-tests mvn clean test -Dcucumber.filter.tags="not @bug" allure:report')
                    
                    if (exitCode != 0) {
                        currentBuild.result = 'UNSTABLE'
                        echo "Tests failed with exit code ${exitCode}"
                    }
                }
            }
        }
        
        stage('Extract Reports') {
            steps {
                script {
                    // Create target directory if it doesn't exist
                    bat 'if not exist target mkdir target'
                    
                    // Copy reports from the container to the workspace
                    // We use || exit 0 to ensure the pipeline continues even if copy fails (e.g. if tests crashed early)
                    bat 'docker cp runner:/app/target/surefire-reports ./target/surefire-reports || exit 0'
                    bat 'docker cp runner:/app/target/cucumber-reports ./target/cucumber-reports || exit 0'
                    bat 'docker cp runner:/app/target/site/allure-maven-plugin ./target/allure-results || exit 0'
                    
                    // Clean up container
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
            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'target/allure-results']]
        }
    }
}

