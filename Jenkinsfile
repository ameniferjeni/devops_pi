pipeline {
    agent any

    environment {
        JAVA_HOME = tool name: 'JDK17', type: 'jdk'
        NODE_HOME = tool name: 'NodeJS20', type: 'nodejs'
        PATH = "${JAVA_HOME}/bin:${NODE_HOME}/bin:${env.PATH}"
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend CI') {
            steps {
                dir('backend') {
                    sh 'mvn -B clean verify'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Frontend CI') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build:ci'
                }
            }
        }

        stage('Docker CD') {
            when {
                branch pattern: 'main|master', comparator: 'REGEXP'
            }
            steps {
                sh 'docker compose build'
                sh 'docker compose up -d'
                sh '''
                  for i in $(seq 1 24); do
                    if curl -sf http://localhost:8081/api/pfe/stats; then
                      exit 0
                    fi
                    sleep 5
                  done
                  exit 1
                '''
            }
            post {
                always {
                    sh 'docker compose down -v || true'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline CI/CD CodingFactory terminée avec succès.'
        }
        failure {
            echo 'Échec du pipeline — consultez les logs des stages Backend, Frontend ou Docker.'
        }
    }
}
