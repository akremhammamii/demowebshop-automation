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
                echo '🐳 Construction de l'image Docker...'
                bat """
                    docker build -t %DOCKER_IMAGE% .
                """
            }
        }

        stage('Run Tests in Docker') {
            steps {
                echo '🧪 Exécution des tests...'
                bat """
                    docker run --rm ^
                        -v "%cd%\\reports:/reports" ^
                        %DOCKER_IMAGE%
                """
            }
        }

        stage('Allure Report') {
            steps {
                echo '📊 Génération du rapport Allure...'
                allure includeProperties: false, jdk: '', results: [[path: 'reports/allure-results']]
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
