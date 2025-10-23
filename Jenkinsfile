pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/ton-utilisateur/ton-projet.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
    }

    post {
        success {
            echo 'Bravo ! Le build a réussi 🎉'
        }
        failure {
            echo 'Le build a échoué ❌'
        }
    }
}
