pipeline {
    agent {
        label 'JavaAgent2'
    }

    //https://www.jenkins.io/doc/book/pipeline/syntax/

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
                sh "docker build . -t ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${
                }"
            }
        }

        stage('Push Image to Nexus') {
            steps {
                echo "Publishing Image to Nexus ${NEXUS_1}"
                sh "echo ${NEXUS_PASSWORD} | docker login ${NEXUS_1} --username ${NEXUS_DOCKER_USERNAME} --password-stdin"
                sh "docker push ${NEXUS_1}/edu.mv/cls515-labmaven-eq19:${VERSION}"
            }
        }

        //https://www.jenkins.io/doc/book/installing/kubernetes/

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
