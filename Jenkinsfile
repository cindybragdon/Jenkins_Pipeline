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
        // Stage 1: Clean & Build
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

        // Stage 2: Compile
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

        // Stage 3: Package
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

        // Stage 4: Test
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

        // Stage 5: Code Coverage
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

        // Stage 6: Docker Build
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

        // Stage 7: Push Image to Nexus
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

        // Stage 8: Connexion SSH
        stage('Connexion SSH') {
            steps {
                script {
                    sshagent(credentials: ['minikube-dev-2-ssh']) {
                        echo "Connexion SSH..."
                        sh '''
                            [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                            ssh ${USER_MINIKUBE}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                            ssh ${USER_MINIKUBE}@${MINIKUBE} "mkdir ${NAMESPACE}"
                            scp -r config/${ENVIRONMENT} ${MINIKUBE}:/home/${USER_MINIKUBE}/${NAMESPACE}
                        '''
                    }
                }
            }
        }

        // Stage 9: Create Namespace
        stage('Create Namespace') {
            when {
                expression { params.NEW_NAMESPACE == true }
            }
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    echo "Creating namespace..."
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_MINIKUBE}@${MINIKUBE} "minikube kubectl -- get namespace ${NAMESPACE} || minikube kubectl -- create namespace ${NAMESPACE}"
                    '''
                }
            }
        }

        // Stage 10: Apply minikube...
        stage('Apply minikube...') {
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    echo "Deploying on Minikube..."
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_MINIKUBE}@${MINIKUBE} "minikube kubectl -- get namespace ${NAMESPACE}"
                        ssh ${USER_MINIKUBE}@${MINIKUBE} "minikube kubectl -- get secrets -n eq19"
                        ssh ${USER_MINIKUBE}@${MINIKUBE} "minikube kubectl -- get secrets -n eq19 && cd ${NAMESPACE}" && ls
                        cd config && cd ${ENVIRONMENT} && ls
                        minikube kubectl -- apply -f . -n ${NAMESPACE}
                    '''
                }
            }
        }
    }
}