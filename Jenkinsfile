
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
        USER_MINIKUBE = user1
        /**
        NEXUS_1 = 'http://10.10.0.30:8081/'
        NEXUS_DOCKER_USERNAME = 'user1'
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
        *//
    }

    stages {
        stage('Connexion ssh'){
            sshagent(credentials : ['minikube-dev-2-ssh']) {

                            //sh "ssh ${USER_KUBE_1}@${MINIKUBE} 'rm -rf ${NAMESPACE}'"
                            //sh "ssh ${USER_KUBE_1}@${MINIKUBE} 'mkdir ${NAMESPACE}'"
                            //sh "ssh ${USER_KUBE_1}@${MINIKUBE} 'scp -r config/${ENV_KUBE} ${USER_KUBE_1}""${MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}'"
                            //echo "connecté"

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

        /**
        stage('Connexion ssh'){
            sshagent(credentials : ['minikube-dev-2']) {
                       echo "connect to ${params.MINIKUBE_2}"
                sh "ssh ${USER_KUBE_1}@${DEPLOY_SERVER} "rm -rf ${EQUIPE}" "
                sh "ssh ${USER_KUBE_1}@${DEPLOY_SERVER} "mkdir ${EQUIPE}" "
                sh "ssh ${USER_KUBE_1}@${DEPLOY_SERVER} "
                sh "scp -r config/${ENV_KUBE} ${DEPLOY_SERVER}:/home/${USER_KUBE_1}/${EQUIPE} "
                sh "echo 'SSH : You are connected' "
        }


        stage('Create namespace') {
                when {
                    expression {
                        params.NEW_NAMESPACE == false
                    }
                }
                steps {
                     sh "ssh user@${USER_KUBE_1} 'minikube kubectl -- create namespace ${EQUIPE}' "
                }
        }


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
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-${EQUIPE}:${VERSION}"
            }
        }

        // Étape 5: Push de l'image Docker vers Nexus
        stage('Push Image to Nexus') {
            steps {
                echo "Publishing Image to Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-${EQUIPE}:${VERSION}"
            }
        }

        // Étape 6: Déploiement sur Minikube (Kubernetes)
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo "Deploying to Minikube: ${params.MINIKUBE}"
                    def minikubeIp
                    if (params.MINIKUBE == 'MINIKUBE_1') {
                        minikubeIp = MINIKUBE_1
                    } else if (params.MINIKUBE == 'MINIKUBE_2') {
                        minikubeIp = MINIKUBE_2
                    } else if (params.MINIKUBE == 'MINIKUBE_3') {
                        minikubeIp = MINIKUBE_3
                    }

                    // Déploiement via SSH
                    sshagent(['minikube-ssh-agent']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no user@${minikubeIp} 'minikube start --vm-driver=none'
                            ssh user@${minikubeIp} 'minikube kubectl -- create namespace ${EQUIPE} || echo "Namespace already exists"'
                            ssh user@${minikubeIp} 'minikube kubectl -- apply -f deployment.yaml --namespace=eq19'
                        """
                    }
                }
            }
        }

        // Étape 7: Création d'un secret Docker registry
        stage('Create Docker Registry Secret') {
            steps {
                script {
                    echo 'Creating Docker Registry Secret in Kubernetes'
                    sh """
                    minikube kubectl -- create secret docker-registry regcred \
                    --docker-server=${NEXUS_1} \
                    --docker-username=${NEXUS_DOCKER_USERNAME} \
                    --docker-password=${NEXUS_PASSWORD} \
                    --docker-email=deploy@company.com \
                    --namespace=${EQUIPE}
                    """
                }
            }
        }

        // Étape 8: Vérification du déploiement
        // Affiche l'état des pods dans le namespace "eq19"
         // Affiche les logs
        stage('Verify Deployment') {
            steps {
                script {
                    echo 'Checking Kubernetes status'
                    sh 'minikube kubectl -- get pods -n ${EQUIPE}'
                    sh 'minikube kubectl -- get events -n ${EQUIPE}'
                }
            }
        }
    }
}
*//
