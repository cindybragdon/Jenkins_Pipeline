pipeline {
    agent { label 'JavaAgent' }

    parameters {
        choice(name: "DEPLOY_SERVER", choices: ["vm", "dev"], description: "Environnement cible")
        choice(name: "MINIKUBE", choices: ["10.10.0.41", "10.10.0.42", "10.10.0.43"], description: "Cluster Minikube")
        choice(name: "SKIP_NAMESPACE", choices: ["Yes", "No"], description: "Créer le namespace")
        choice(name: "SKIP_PUSH", choices: ["Yes", "No"], description: "Pousser l'image")
    }

    environment {
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        NAMESPACE = "eq19"
        ENV_KUBE = "${params.DEPLOY_SERVER}"
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
    }

    stages {
        stage('Compile et package') {
            when { expression { params.SKIP_PUSH == "No" } }
            steps {
                sh 'mvn clean compile package'
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

        stage('Créer le namespace') {
            when { expression { params.SKIP_NAMESPACE == "No" } }
            steps {
                sh """
                    ssh ${USER_KUBE_1}@${params.MINIKUBE} "minikube kubectl -- create namespace ${NAMESPACE}"
                """
            }
        }

        stage('Déployer sur Kubernetes') {
            steps {
                sshagent(credentials: ['minikube-dev-2-ssh']) {
                    sh """
                        scp -r config/${ENV_KUBE} ${USER_KUBE_1}@${params.MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}
                        ssh ${USER_KUBE_1}@${params.MINIKUBE} "minikube kubectl -- apply -f /home/${USER_KUBE_1}/${NAMESPACE}/config/${ENV_KUBE} --namespace=${NAMESPACE}"
                    """
                }
            }
        }
    }
}
