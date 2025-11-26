pipeline {
    agent any
    
    environment {
        // Ensure tests run in headless mode
        MAVEN_OPTS = '-Xmx1024m -Dmaven.repo.local=$WORKSPACE/.m2/repository'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Explicitly checkout source code before building Docker image
                checkout scm
                echo 'Code checked out successfully'
                // Verify Dockerfile exists
                script {
                    if (isUnix()) {
                        sh 'ls -la .devcontainer/Dockerfile.dev'
                    } else {
                        bat 'dir .devcontainer\\Dockerfile.dev'
                    }
                }
            }
        }
        
        stage('Test in Docker') {
            agent {
                dockerfile {
                    filename '.devcontainer/Dockerfile.dev'
                    args '--shm-size=2g'
                    // Important: Run on the same node where we checked out the code
                    reuseNode true
                }
            }
            steps {
                echo 'Compiling project...'
                sh 'mvn clean compile'
                
                echo 'Running Cucumber tests...'
                sh 'mvn test -Dcucumber.filter.tags="not @bug"'
            }
        }
        
        stage('Generate Reports') {
            agent {
                dockerfile {
                    filename '.devcontainer/Dockerfile.dev'
                    reuseNode true
                }
            }
            steps {
                echo 'Generating Allure report...'
                sh 'mvn allure:report || true'
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
        
        success {
            echo 'Tests passed successfully! ✅'
        }
        
        failure {
            echo 'Tests failed! ❌'
        }
    }
}

