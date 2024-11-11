
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
        USER_MINIKUBE = 'user1'

    }

    stages {
        stage('Connexion ssh'){
            steps{
                script{
                    sshagent(credentials : ['minikube-dev-2-ssh']) {
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
        }
    }
}