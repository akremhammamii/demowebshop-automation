pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "demowebshop-tests"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📥 Récupération du code source...'
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Construction de l’image Docker...'
                bat """
                    docker build -t %DOCKER_IMAGE% .
                """
            }
        }

        stage('Run Tests in Docker') {
            steps {
                script {
                    echo '🧪 Exécution des tests...'
                    
                    // Remove any existing container
                    bat 'docker rm -f demowebshop-runner || exit 0'
                    
                    // Run tests (allow failure to extract reports)
                    def exitCode = bat(
                        returnStatus: true,
                        script: 'docker run --name demowebshop-runner %DOCKER_IMAGE%'
                    )
                    
                    // Clean old reports and create target directory
                    bat 'if exist target rmdir /s /q target'
                    bat 'mkdir target'
                    bat 'docker cp demowebshop-runner:/app/target/surefire-reports ./target/surefire-reports || exit 0'
                    bat 'docker cp demowebshop-runner:/app/target/cucumber-reports ./target/cucumber-reports || exit 0'
                    bat 'docker cp demowebshop-runner:/app/target/allure-results ./target/allure-results || exit 0'
                    
                    // Cleanup container
                    bat 'docker rm -f demowebshop-runner'
                    
                    // Mark build as unstable if tests failed
                    if (exitCode != 0) {
                        currentBuild.result = 'UNSTABLE'
                        echo "⚠️ Tests failed with exit code ${exitCode}"
                    }
                }
            }
        }

        stage('Publish Reports') {
            steps {
                echo '📊 Publication des rapports...'
                
                // Publish test results
                junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                
                // Archive artifacts
                archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
                
                // Generate Allure report
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            echo '🧹 Nettoyage...'
            bat "docker system prune -f"
        }
    }
}
