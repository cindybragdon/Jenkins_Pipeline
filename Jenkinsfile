pipeline {
    agent { label 'JavaAgent' }

    parameters {
        choice(name: "DEPLOY_SERVER", choices: ["vm", "dev"], description: "Environnement cible")
        choice(name: "MINIKUBE", choices: ["10.10.0.41", "10.10.0.42", "10.10.0.43"], description: "Cluster Minikube")
        choice(name: "SKIP_NAMESPACE", choices: ["Yes", "No"], description: "Créer le namespace")
        choice(name: "SKIP_PUSH", choices: ["Yes", "No"], description: "Pousser l'image")
    }

    environment {
        NAME = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        GROUP_ID = readMavenPom().getGroupId()
        NAMESPACE = "eq19"
        ENV_KUBE = "${params.DEPLOY_SERVER}"
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
    }

    stages {
        stage('Compile et package') {
            when { expression { params.SKIP_PUSH == "No" } }
            steps {
                sh 'mvn clean package'
                sh 'mvn clean compile'
            }
        }

        stage('install') {
            when { expression { params.SKIP_PUSH == "No" } }
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build et push Docker') {
            when { expression { params.SKIP_PUSH == "No" } }
            steps {
                sh """
                    docker build -t ${NEXUS_1}/edu.mv/${IMAGE}:${VERSION} .
                    echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin
                    docker push ${NEXUS_1}/edu.mv/${IMAGE}:${VERSION}
                """
            }
        }

        stage('Setup working environment ') {
            steps {

                script {
                    if (params.DEPLOY_SERVER == 'dev') {
                        env.IP_IMAGE = '10.10.0.30:8082'
                    } else if (params.DEPLOY_SERVER == 'vm') {
                        env.IP_IMAGE = '192.168.107.135:8082'
                    }
                    env.IMAGE = "${env.IP_IMAGE}/${GROUP_ID}/${NAME}/${VERSION}"
                }
                sh """
                    envsubst < config/deployment_modif.yaml > config/deploy/deployment.yaml
                    envsubst < config/service_modif.yaml > config/deploy/service.yaml
                """
            }
        }

        stage('Créer le namespace') {
            when { expression { params.SKIP_NAMESPACE == "No" } }
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- create namespace ${NAMESPACE}"
                    '''
                }
            }
        }

        stage('Déployer sur Kubernetes') {
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_KUBE_1}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                        ssh ${USER_KUBE_1}@${MINIKUBE} "mkdir ${NAMESPACE}"
                        ssh ${USER_KUBE_1}@${MINIKUBE} "ls"
                        docker images
                        scp -r config/${ENV_KUBE} ${MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}

                    '''

                    sh '''
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        ssh ${USER_KUBE_1}@${MINIKUBE} "cd ${NAMESPACE}" && ls && cd config && cd deploy && ls && minikube kubectl -- apply -f . --namespace=${NAMESPACE}
                    '''
                }
            }
        }
    }
}
