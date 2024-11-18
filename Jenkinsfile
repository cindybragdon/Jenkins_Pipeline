pipeline {
    agent {
        label 'JavaAgent'
    }

    parameters {
        choice(
            name: "DEPLOY_SERVER",
            choices: ["vm", "dev"],
            description: "Choix de l'environnement"
        )
        choice(
            name: "MINIKUBE",
            choices: ["10.10.0.41", "10.10.0.42", "10.10.0.43"],
            description: "Minikube de déploiement"
        )
        choice(
            name: "SKIP_NAMESPACE",
            choices: ["Yes", "No"],
            description: "Creating the namespace"
        )
        choice(
            name: "SKIP_PUSH",
            choices: ["Yes", "No"],
            description: "Skip push"
        )
    }

    environment {
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        NAMESPACE = "eq1999"
        IP_NEXUS_VM = "192.168.5.129"
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
    }

    stages {
        stage('compile') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                sh 'mvn compile'
            }
        }

        stage('package') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                sh 'mvn package'
            }
        }

        stage('test') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('JaCoCo Report') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                jacoco(
                    execPattern: '**/target/**.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src',
                    inclusionPattern: '**/*.class',
                    changeBuildStatus: true,
                    minimumInstructionCoverage: '60'
                )
            }
        }

        stage('docker build') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                echo 'Building Image edu.mv/cls515-labmaven-eq199'
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq199:${VERSION}"
            }
        }

        stage('push image to Nexus') {
            when {
                expression { params.SKIP_PUSH == "No" }
            }
            steps {
                echo "Publication de l'image sur Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq199:${VERSION}"
            }
        }

        stage('Connect to ssh') {
            steps {
                script {
                    echo "Connect to Minikube..."
                    sshagent(credentials: ['minikube-dev-2-ssh']) {
                        echo "connect to ${params.MINIKUBE}"
                        sh '''
                            [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                            ssh ${USER_KUBE_1}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                            ssh ${USER_KUBE_1}@${MINIKUBE} "mkdir ${NAMESPACE}"
                            ssh ${USER_KUBE_1}@${MINIKUBE} "ls"
                            scp -r config/${ENV_KUBE} ${MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}
                        '''
                    }
                }
            }
        }

        stage('Create namespace...') {
            when {
                expression { params.SKIP_NAMESPACE == "No" }
            }
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    echo "Creating namespace..."
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
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    echo "Deploying on Minikube..."
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_KUBE_1}@${MINIKUBE} "cd ${NAMESPACE}" && ls && cd config && cd dev && ls && minikube kubectl -- apply -f . --namespace=${NAMESPACE}
                    '''
                }
            }
        }
    }
}
