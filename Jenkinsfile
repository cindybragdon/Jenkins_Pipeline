pipeline {
    agent {
        label 'JavaAgent2'
    }

    //https://www.jenkins.io/doc/book/pipeline/syntax/
    //https://help.skytap.com/connect-to-a-linux-vm-with-ssh.html
    //https://www.jenkins.io/doc/book/installing/kubernetes/


    parameters {
        choice(name: 'ENVIRONMENT', choices: ['vm', 'dev'], description: 'Choice of deployment environment: vm or dev')
        choice(name: 'MINIKUBE', choices: ['MINIKUBE_1', 'MINIKUBE_2', 'MINIKUBE_3' ], description: 'Choice of Minikube')
    }

    environment {
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()

        NEXUS_1 = 'http://10.10.0.30:8081/'
        NEXUS_DOCKER_USERNAME = 'user1'
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')

        MINIKUBE_1 = '10.10.0.41'
        MINIKUBE_2 = '10.10.0.42'
        MINIKUBE_3 = '10.10.0.43'
    }

    stages {

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

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

        stage('Build') {
            steps {
                sh "mvn install"
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker Image'
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

        stage('Push Image to Nexus') {
            steps {
                echo "Publishing Image to Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }


        stage('Deploy to Kubernetes') {
            steps {
                script {
                    if (params.ENVIRONMENT == 'vm') {
                        echo 'Deploying to vm'
                        steps {
                                script {
                                  kubernetesDeploy(configs: "deployment.yaml",
                                                                 "service.yaml")
                                }

                    } else if (params.ENVIRONMENT == 'dev') {
                        echo 'Deploying to server dev'

                        // Setup deployment to CMV remote server
                        sh "kubectl config use-context serverMV-context"

                        // Apply the Kubernetes deployment configuration
                        sh "kubectl apply -f deployment.yaml"

                        // Update image in deployment
                        sh "kubectl set image deployment/my-deployment my-container=${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
                    }
                }
            }
        }
    }
}

/**
pipeline {
    agent { label 'JavaAgent2' }

    environment {
        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD') // Récupère le mot de passe Nexus à partir des credentials Jenkins pour les déploiements sécurisés.
        NEXUS_URL = "${NEXUS_1}" // Déclare l'URL de Nexus en utilisant une variable d'environnement prédéfinie.
        DOCKER_USERNAME = "${NEXUS_DOCKER_USERNAME}" // Utilise le nom d'utilisateur Docker pour Nexus, stocké dans une variable d'environnement.
        MINIKUBE_IP = "${MINIKUBE_1}" // Stocke l'adresse IP de Minikube depuis une variable d'environnement.
        ENV_KUBE = "${ENV_KUBE}" // Définit l'environnement Kubernetes (par exemple, "dev" ou "vm") depuis une variable d'environnement.
    }

    stages {

        stage('Git Clone repertoire code source') {
            steps {
                git 'git@github.com:420-515-MV/cls515-labmaven-eq19.git' // Clone le dépôt GitHub vers Jenkins.
            }
        }

        stage('Clean et Build du projet Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build de limage Docker') {
            steps {
                sh "docker build . -t ${NEXUS_URL}/edu.mv/cls515-labmaven-eq19:${VERSION}" // Image Docker avec un tag de version
            }
        }

        stage('Push de limage Docker sur Nexus') {
                    steps {
                        withCredentials([usernamePassword(credentialsId: 'NEXUS_CREDENTIALS', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) { // Connexion sécurisée grace aux credentials Jenkins pour Nexus.
                            sh "echo $NEXUS_PASSWORD | docker login ${NEXUS_URL} -u $DOCKER_USERNAME --password-stdin" // Connexion à Nexus en utilisant Docker.
                            sh "docker push ${NEXUS_URL}/edu.mv/cls515-labmaven-eq19:${VERSION}" // Push l'image Docker vers le dépôt Nexus.
                        }
                }
         }

        stage('Setup Minikube pour le deploiement Kubernetes') {
            steps {
                script {
                    if (ENV_KUBE == 'dev') { // Si l'environnement est "dev"
                        sh 'ssh-agent -k' // Stop agent SSH en cours d'exécution
                        sshagent(['ssh-minikube-3']) { // Utilise les identifiants SSH pour Minikube 3
                            sh 'minikube start' // Start Minikube.
                            sh 'minikube kubectl -- create namespace eq19 || echo "Namespace already exists"' // Crée le namespace si il n'existe pas déja.
                        }
                    } else { // Si l'environnement n'est pas "dev" donc vm
                        sh 'ssh-agent -k' // Stop agent SSH en cours d'exécution
                        sshagent(['agent-dev-2-ssh']) { // Utilise les identifiants SSH pour Minikube 2.
                            sh 'minikube start' // Start Minikube.
                            sh 'minikube kubectl -- create namespace eq19 || echo "Namespace already exists"' // Crée le namespace si il n'existe pas déja.
                        }
                    }
                }
            }
        }

            stage('Creation du secret Docker registry') { // Crée un secret Docker pour authentifier Kubernetes sur le dépôt Nexus dans le namespace "eq19".
                steps {
                    sh """
                    minikube kubectl -- create secret docker-registry regcred \
                    --docker-server=${NEXUS_URL} \
                    --docker-username=$DOCKER_USERNAME \
                    --docker-password=$NEXUS_PASSWORD \
                    --docker-email=de@deploy.com \
                    --namespace=eq19
                    """
                }
            }

            stage('Deploiement sur Kubernetes') {
                steps {
                    dir('config/vm') { // Change le répertoire vers "config/vm" (ou "dev" ).
                        sh 'minikube kubectl -- apply -f deployment.yaml --namespace=eq19' // Fichier YAML de déploiement pour créer les ressources.
                        sh 'minikube kubectl -- apply -f service.yaml --namespace=eq19' // Fichier YAML de service pour exposer les services.
                    }
                }
            }

            stage('Verif apres le deploiement') { // Étape pour vérifier le statut après le déploiement.
                steps {
                    script {
                        sh 'minikube kubectl -- get events -n eq19 --sort-by=\'.metadata.creationTimestamp\'' // Affiche les logs du namespace "eq19" pour vérifier l'ordre de création.
                        sh 'minikube kubectl -- get pods -n eq19' // Affiche les pods dans le namespace "eq19" pour vérifier leur statut.
                    }
                }
            }
        }
    }
**/