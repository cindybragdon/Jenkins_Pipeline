pipeline {
    agent {
        label 'JavaAgent2'
    }

    //parameters {
     //   choice(name: 'DEPLOY_SERVER', choices: ['10.10.0.43','10.10.0.42','10.10.0.41'], description: 'Serveur?')
     //   string(name: 'SSH_AGENT_CRED', defaultValue: 'ssh-minikube-3', description: 'Quel ssh agent credentials?')
     //   booleanParam(name: 'NEW_NAMESPACE', defaultValue: false, description: 'Nouveau namespace?')
     //   booleanParam(name: 'SKIP_PUSH', defaultValue: true, description: 'Skip Nexus?')
  //  }

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


        stage('compile') {
            steps {
                sh 'mvn compile'
            }
        }




        stage('test') {
            steps {
                sh 'mvn test'
            }
        }
        // Référence --> https://www.lambdatest.com/blog/jenkins-declarative-pipeline-examples/
        // Mécanisme qui bloque la création d'une image docker si la couverture de test est en deça de 60%
        stage("Code coverage") {
                   steps {
                       jacoco(
                            execPattern: '**/target/**.exec',
                            classPattern: '**/target/classes',
                            sourcePattern: '**/src',
                            inclusionPattern: '**/*.class',
                            changeBuildStatus: true,
                            minimumInstructionCoverage: '60',
                            maximumInstructionCoverage: '100')
                       }
                   }

         stage('build') {
                    steps {
                        sh "mvn install"
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