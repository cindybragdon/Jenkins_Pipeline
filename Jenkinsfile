pipeline {
    agent {
        label 'JavaAgent'
<<<<<<< HEAD
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
=======
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
>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
    environment {
        NAME = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
        GROUP_ID = readMavenPom().getGroupId()
<<<<<<< HEAD
        NAMESPACE = "eq19"
        IP_NEXUS_VM = "192.168.5.129"

        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')
        NVD_API_KEY = credentials('NVD-API-KEY')
=======
        NAMESPACE = "eq8"
        IP_NEXUS_VM = "192.168.5.129"

        NEXUS_PASSWORD = credentials('DEPLOY_USER_PASSWORD')

>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
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
<<<<<<< HEAD

=======
>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
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
<<<<<<< HEAD
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
            when {
                expression {
                    params.SKIP_PUSH == "No"
                }
            }
            steps {
                script {

                    dependencyCheck additionalArguments: """
                        --nvdApiKey ${NVD_API_KEY}
                        --nvdApiDelay 16000
                        --scan .
                        --format ALL
                        --out .
                    """, odcInstallation: 'DP-Check'
                }

                dependencyCheckPublisher(
                    pattern: '**/dependency-check-report.xml'
                )
=======
>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
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
<<<<<<< HEAD
                    env.IMAGE = "${env.IP_IMAGE}/${GROUP_ID}/${NAME}:${VERSION}"
=======
                    env.IMAGE = "${env.IP_IMAGE}/${GROUP_ID}/${NAME}/${VERSION}"
>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
                }
                sh """
                    envsubst < config/deployment_modif.yml > config/deploy/deployment.yml
                    envsubst < config/service_modif.yml > config/deploy/service.yml
                """
            }
        }

        stage('Minikube Operations') {
<<<<<<< HEAD
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
                                      ssh ${USER_KUBE_1}@${MINIKUBE} "cd ${NAMESPACE} && cd deploy && cat deployment.yaml && minikube kubectl -- apply -f . --namespace=${NAMESPACE}  "
                                      ssh ${USER_KUBE_1}@${MINIKUBE} "minikube service list"
                                      ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- get pods -n ${NAMESPACE}"
                                      ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- get pods -A"
                                      ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- get services -n ${NAMESPACE}"

                                """


                                if (params.CLEAR_NAMESPACE == "Yes") {
                                    sh """
                                        ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete all --all -n ${NAMESPACE}"
                                        ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete pvc,secret,configmap,service,deployment,pod,statefulset --all -n ${NAMESPACE}"
                                        ssh ${USER_KUBE_1}@${MINIKUBE} "minikube kubectl -- delete namespace ${NAMESPACE}"
                                    """
                                }

                                def serviceUrl = sh(script: """
                                    ssh ${USER_KUBE_1}@${MINIKUBE} "minikube service ${NAME} -n ${NAMESPACE} --url"
                                """, returnStdout: true).trim()
                                echo "Service accessible at: ${serviceUrl}"

                                def createRocket = sh(script: """
                                    ssh ${USER_KUBE_1}@${MINIKUBE} "curl -X POST ${serviceUrl}/rocket -H 'Content-Type: application/json' -d '{\\\"id\\\":1,\\\"name\\\": \\\"Interstellar\\\", \\\"type\\\": \\\"sinucoïdal\\\"}'"
                                """, returnStatus: true)
                                if (createRocket != 0) {
                                    error("Failed to create a rocket")
                                } else {
                                    echo "Fusée en lancement!!!"
                                }

                                def getRocket = sh(script: """
                                    ssh ${USER_KUBE_1}@${MINIKUBE} "curl -X GET ${serviceUrl}/rocket/1"
                                """, returnStatus: true)
                                if (getRocket != 0) {
                                    error("Failed to retrieve rockets")
                                } else {
                                    echo "SUCCES !!! GET rocket a fonctionné pppppiiiiissshhhh"
                                }


                            }
                        }
                    }
                }
        stage('Insectes et Bugs') {
                    steps {

                            script {
                                echo "🐜 Exploration des fourmis..."

                                echo "🦗 Les criquets sautent dans la file..."

                                echo "🐞 Une coccinelle élégante passe devant."

                                echo "🐝 Une abeille bourdonnante rejoint la fête."

                                echo "🕷️ Une araignée tisse une file d'attente spéciale."

                                echo "🪲 Un scarabée scintillant arrive en dernier."

                                echo "Avec Jenkins, même les bugs font la queue ! 🐛🐜🦋🪲🐝 "
                            }
                        }
                    }

=======
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
>>>>>>> 4766fe30fddcfd82995d63b8fa22d28703d1ab6e
    }
}