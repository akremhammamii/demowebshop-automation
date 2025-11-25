pipeline {
    agent {
        dockerfile {
            // Jenkins will build the image from this Dockerfile and run tests inside it
            filename '.devcontainer/Dockerfile.dev'
            // Additional Docker arguments for headless execution
            args '--shm-size=2g'
        }
    }
    
    environment {
        // Ensure tests run in headless mode
        MAVEN_OPTS = '-Xmx1024m -Dmaven.repo.local=$WORKSPACE/.m2/repository'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Git checkout is automatic in declarative pipeline
                echo 'Code checked out from SCM'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compiling project...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running Cucumber tests...'
                // Run tests excluding known bugs
                sh 'mvn test -Dcucumber.filter.tags="not @bug"'
            }
        }
        
        stage('Generate Reports') {
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
            
            // Publish Allure report (requires Allure Jenkins plugin)
            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'target/allure-results']]
        }
        
        success {
            echo 'Tests passed successfully! ✅'
        }
        
        failure {
            echo 'Tests failed! ❌'
            // You can add notifications here (email, Slack, etc.)
        }
    }
}

