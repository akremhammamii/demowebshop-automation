pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "demowebshop-tests"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📥 Récupération du code source..."
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🏗️ Construction de l'image Docker..."
                bat """
                    docker build -t %DOCKER_IMAGE% .
                """
            }
        }

        stage('Run Tests in Docker') {
            steps {
                echo "🧪 Exécution des tests..."
                bat """
                    docker run --rm ^
                        -v "%WORKSPACE%\\allure-results:/app/allure-results" ^
                        %DOCKER_IMAGE%
                """
            }
        }

        stage('Archive Reports') {
            steps {
                echo "📊 Archivage des rapports Allure..."
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }
        }
    }

    post {
        always {
            echo "🧹 Nettoyage..."
            bat """
                docker system prune -f
            """
        }
        failure {
            echo "❌ Le pipeline a échoué."
        }
        success {
            echo "✅ Pipeline terminé avec succès."
        }
    }
}
