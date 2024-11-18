// https://www.jenkins.io/doc/book/pipeline/syntax/
// https://help.skytap.com/connect-to-a-linux-vm-with-ssh.html
// https://www.jenkins.io/doc/book/installing/kubernetes/

pipeline {
    agent {
        label 'JavaAgent'
    }

    parameters {
        choice(
            name: 'ENVIRONMENT', 
            choices: ['vm', 'dev'], 
            description: 'Choice of deployment environment: vm or dev'
        )
        choice(
            name: 'MINIKUBE', 
            choices: ['10.10.0.41', '10.10.0.42', '10.10.0.43'],
            description: 'Choice of Minikube'
        )
        booleanParam(
            name: 'NEW_NAMESPACE', 
            defaultValue: false, 
            description: 'Nouveau namespace?'
        )
        booleanParam(
            name: 'SKIP_PUSH', 
            defaultValue: true, 
            description: 'Skip Nexus?'
        )
    }

    environment {
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        NAMESPACE = 'eq19'
        USER_MINIKUBE = 'user1'
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
    }

    stages {
        // Étape 1: Nettoyage et construction du projet Maven
        stage('Clean & Build') {
            when {
                  expression {
                       params.SKIP_PUSH == false
                  }
            }
            steps {
                sh 'mvn clean install'
            }
        }

        // Étape 2: Compilation
        stage('Compile') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
            steps {
                sh 'mvn compile'
            }
        }

        // Étape 3: Emballage
        stage('Package') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
            steps {
                sh 'mvn package'
            }
        }

        // Étape 4: Tests
        stage('Test') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
            steps {
                sh 'mvn test'
            }
        }

        // Étape 5: Couverture de code
        stage('Code Coverage') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
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

        // Étape 6: Création de l'image Docker avec le tag versionné
        stage('Docker Build') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
            steps {
                echo 'Building Docker Image'
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

        // Étape 7: Push de l'image Docker vers Nexus
        stage('Push Image to Nexus') {
            when {
                expression {
                    params.SKIP_PUSH == false
                }
            }
            steps {
                echo "Publishing Image to Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

        // Étape 8: Connexion SSH
        stage('Connexion SSH') {
            steps {
                script {
                    sshagent(credentials: ['minikube-dev-2-ssh']) {
                    echo "Connexion SSH  ..."
                        sh '''
                            [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                            ssh ${USER_MINIKUBE}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                            ssh ${USER_MINIKUBE}@${MINIKUBE} "mkdir ${NAMESPACE}"
                            scp -r config/${ENV_KUBE} ${MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}
                        '''
                    }
                }
            }
        }

        // Étape 9: Création du namespace
        stage('Create Namespace') {
            when {
                expression {
                    params.NEW_NAMESPACE == true
                }
            }
            steps {
                        sshagent(credentials: ['minikube-dev-2-ssh']) {
                            echo "Creating namespace ..."
                            sh '''
                                [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                                ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- create namespace ${NAMESPACE}"
                            '''
                }
            }
        }
        stage('Apply minikube...') {
                    steps {
                        sshagent(credentials : ['minikube-dev-2-ssh']) {
                            echo "Deploying  on Minikube..."
                            sh '''
                                  [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                                  ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                                  ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- get namespace ${NAMESPACE}"
                                  ssh ${USER_KUBE_1}@${MINIKUBE} "cd ${NAMESPACE}" && ls && cd config && cd dev && ls && minikube kubectl -- apply -f . -n ${NAMESPACE}

                            '''
                        }
                    }
        }
    }
}
