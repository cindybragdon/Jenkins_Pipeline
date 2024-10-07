pipeline {
    agent {
        label 'AgentJava'
    }

    environment {
    IMAGE = readMavenPom().getArtifactId()
    VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stages {
                stage('compile') {
                    steps {
                        sh 'mvn compile'
                    }
                }


        stage('build') {
                    steps {
                        sh "mvn install"
                    }
                }

        stage('test') {
                    steps {
                        sh 'mvn test'
                    }
                }


        stage('package') {
                   steps {
                       sh 'mvn package'
                   }
               }

        stage('docker build') {
            steps {
                echo 'Building Image edu.mv/cls515-labmaven-eq19'
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

        stage('push image to Nexus') {
            steps {
                echo 'Publication de Image sur Nexus ${NEXUS_1}'
                sh "echo ${NEXUS_DOCKER_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

    }
}