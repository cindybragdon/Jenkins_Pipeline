pipeline {
    agent {
        label 'JavaAgent'
    }

    parameters {
         choice(name:"DEPLOY_SERVER", choices:["vm", "dev"], description:"Choix de l'environement")
         choice(name:"MINIKUBE", choices:["10.10.0.41", "10.10.0.42","10.10.0.43"], description:"Minikube de déploiement")
         choice(name:"CREATE_NAMESPACE", choices:["No","Yes"], description:"Creating the namespace")
         choice(name:"CREATE_SECRET", choices:["No","Yes"], description:"Creating the secret")
         choice(name:"SKIP_PUSH", choices:["Yes","No"], description:"Skip push")
         choice(name:"SKIP_MINIKUBE", choices:["No","Yes"], description:"Skip Minikube")
         choice(name:"CLEAR_NAMESPACE", choices:["No","Yes"], description:"CLEAR NAMESPACE")
    }
    environment {
        NAME = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        GROUP_ID = readMavenPom().getGroupId()
        NAMESPACE = "eq8"
        IP_NEXUS_VM = "192.168.5.129"

        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')

    }
    stages {

        stage('compile') {
            when{
                 expression {
                    params.SKIP_PUSH == "No"
                 }
            }
            steps {
                sh 'mvn compile'
            }
        }

        stage('package') {
           when{
                expression {
                    params.SKIP_PUSH == "No"
                }
           }
           steps {
               sh 'mvn package'
           }
        }
        stage('test') {
           when{
                expression {
                    params.SKIP_PUSH == "No"
                }
           }
           steps {
                sh 'mvn test'
           }
        }
        stage('JaCoCo Report') {
            when{
                 expression {
                    params.SKIP_PUSH == "No"
                 }
            }
            steps {
                jacoco(
                    execPattern: '**/target/**.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src',
                    inclusionPattern:'**/*.class',
                    changeBuildStatus:true,
                    minimumInstructionCoverage:'60'
                )
            }
        }
        stage('docker build') {
            when{
                expression {
                    params.SKIP_PUSH == "No"
                }
            }
            steps {
                echo 'Building Image edu.mv/cls515-labmaven-eq8'
                sh "docker build . -t ${NEXUS_1}/${GROUP_ID}/${NAME}:${VERSION}"
            }
        }

        stage('push image to Nexus') {
            when{
                expression {
                    params.SKIP_PUSH == "No"
                }
            }
            steps {
                echo "Publication de Image sur Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/${GROUP_ID}/${NAME}:${VERSION}"
            }
        }

        stage('Setup working environment ') {
            when{
                 expression {
                    params.SKIP_MINIKUBE == "No"
                 }
            }
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
                    envsubst < config/deployment_modif.yml > config/deploy/deployment.yaml
                    envsubst < config/service_modif.yml > config/deploy/service.yaml
                """
            }
        }

        stage('Minikube Operations') {
            when { expression { params.SKIP_MINIKUBE == "No" } }
            steps {
                sshagent(['minikube-dev-2-ssh']) {
                    script {

                        sh """
                            [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -t rsa,dsa ${MINIKUBE} >> ~/.ssh/known_hosts
                        """


                        if (params.CREATE_NAMESPACE == "Yes") {
                            sh """
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- create namespace ${NAMESPACE}"
                            """
                        }


                        if (params.CREATE_SECRET == "Yes") {
                            sh """
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- create secret docker-registry regcred --docker-server=${MINIKUBE}:8082 --docker-username=deploy-user --docker-password=Pass123! --docker-email=de@deploy.com --namespace=${NAMESPACE}"
                            """
                        }


                        sh """
                              ssh ${USER_KUBE_1}@${MINIKUBE} "rm -rf ${NAMESPACE}"
                              ssh ${USER_KUBE_1}@${MINIKUBE} "mkdir ${NAMESPACE}"
                              scp -r config/deploy ${MINIKUBE}:/home/${USER_KUBE_1}/${NAMESPACE}
                              ssh ${USER_KUBE_1}@${MINIKUBE} "cd ${NAMESPACE} && cd deploy && minikube kubectl -- apply -f . --namespace=${NAMESPACE}"
                        """


                        if (params.CLEAR_NAMESPACE == "Yes") {
                            sh """
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete all --all -n ${NAMESPACE}"
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete pvc,secret,configmap,service,deployment,pod,statefulset --all -n ${NAMESPACE}"
                                ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete namespace ${NAMESPACE}"
                            """
                        }
                    }
                }
            }
        }
        stage('Insectes et Bugs') {
                    steps {
                        ansiColor('xterm') { // Active le support pour la couleur et le formatage
                            script {
                                echo "🐜 Exploration des fourmis..."
                                sleep(2)
                                echo "🦗 Les criquets sautent dans la file..."
                                sleep(2)
                                echo "🐞 Une coccinelle élégante passe devant."
                                sleep(2)
                                echo "🐝 Une abeille bourdonnante rejoint la fête."
                                sleep(2)
                                echo "🕷️ Une araignée tisse une file d'attente spéciale."
                                sleep(2)
                                echo "🪲 Un scarabée scintillant arrive en dernier."
                                sleep(2)

                                // Affiche la phrase en gras et en plus grand
                                echo "\033[1;34m\033[4m\033[5m Avec Jenkins, même les bugs font la queue ! 🐛🐜🦋🪲🐝 \033[0m"
                            }
                        }
                    }
                }
    }
}