
    //https://www.jenkins.io/doc/book/pipeline/syntax/
    //https://help.skytap.com/connect-to-a-linux-vm-with-ssh.html
    //https://www.jenkins.io/doc/book/installing/kubernetes/

pipeline {
    agent {
        label 'JavaAgent2'
    }

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['vm', 'dev'], description: 'Choice of deployment environment: vm or dev')
        choice(name: 'MINIKUBE', choices: ['MINIKUBE_1', 'MINIKUBE_2', 'MINIKUBE_3'], description: 'Choice of Minikube')
        booleanParam(name: 'NEW_NAMESPACE', defaultValue: false, description: 'Nouveau namespace?')
    }

    environment {
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        NAMESPACE = 'eq19'
        USER_MINIKUBE = 'user1'

    }

    stages {
     // Étape 2: Nettoyage et construction du projet Maven
            stage('Clean & Build') {
                steps {
                    sh 'mvn clean install'
                }
            }

            // Étape 3: Code Coverage
            stage('Code Coverage') {
                steps {
                    jacoco(
                        execPattern: '**/target/**.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src',
                        inclusionPattern: '**/*.class',
                        changeBuildStatus: true,
                        minimumInstructionCoverage: '60',
                        maximumInstructionCoverage: '100'
                    )
                }
            }

            // Étape 4: Création de l'image Docker avec le tag versionné
            stage('Docker Build') {
                steps {
                    echo 'Building Docker Image'
                    sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
                }
            }

            // Étape 5: Push de l'image Docker vers Nexus
            stage('Push Image to Nexus') {
                steps {
                    echo "Publishing Image to Nexus ${NEXUS_1}"
                    sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                    sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
                }
            }
        stage('Connexion ssh'){
            steps{
                script{
                    sshagent(credentials : ['minikube-dev-2-ssh']) {
                                   sh '''
                                          [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                                          ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                                          ssh ${USER_MINIKUBE}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                                          ssh ${USER_MINIKUBE}@${MINIKUBE} "mkdir ${NAMESPACE}"
                                          ssh ${USER_MINIKUBE}@${MINIKUBE} "scp -r config/${ENV_KUBE} ${MINIKUBE}:/home/${USER_MINIKUBE}/${NAMESPACE}"

                                    '''
                      }
                }
            }
        }
    }
}