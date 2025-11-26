pipeline {
    agent any

    environment {
        IMAGE_NAME = "demowebshop-tests"
        CONTAINER_NAME = "demowebshop-container"
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
                echo "🐳 Construction de l'image Docker de test..."
                script {
                    sh """
                        docker build -t ${IMAGE_NAME} -f Dockerfile.dev .
                    """
                }
            }
        }

        stage('Run Tests in Docker') {
            steps {
                echo "🧪 Exécution des tests dans Docker..."
                script {
                    sh """
                        docker run --rm \
                            -v \$(pwd):/workspace \
                            -w /workspace \
                            ${IMAGE_NAME} mvn clean test
                    """
                }
            }
        }

        stage('Archive Reports') {
            steps {
                echo "📦 Archivage des rapports..."
                junit 'target/surefire-reports/*.xml'
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            echo "🧹 Nettoyage..."
            sh "docker rm -f ${CONTAINER_NAME} 2>/dev/null || true"
        }
        success {
            echo "✔️ Pipeline terminé avec succès."
        }
        failure {
            echo "❌ Le pipeline a échoué."
        }
    }
}
