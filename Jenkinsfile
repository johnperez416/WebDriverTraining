pipeline {
    agent any
    tools {
        maven 'Maven 3.5.4'
        jdk 'jdk8'
    }
    stages {
        stage ('Package') {
            steps {
                sh """
                    mvn -Dmaven.test.skip=true package
                """
            }
        }
        stage ('UI Testing') {
            steps {
                withCredentials([
                  string(credentialsId: 'OctopusAPIKey', variable: 'APIKey'),
                  string(credentialsId: 'OctopusServer', variable: 'OctopusServer')
                ]) {
                    sh """
                        ${tool('Octo CLI')}/Octo push \
                            --package ${WORKSPACE}/target/webdrivertraining.1.0-SNAPSHOT.jar \
                            --replace-existing \
                            --server ${OctopusServer} \
                            --apiKey ${APIKey}
                        ${tool('Octo CLI')}/Octo create-release \
                            --project WebDriverLambda \
                            --channel ${env.BRANCH_NAME} \
                            --ignoreexisting \
                            --package webdrivertraining:1.0-SNAPSHOT.jar \
                            --version 1.0.${env.BUILD_NUMBER} \
                            --server ${OctopusServer} \
                            --apiKey ${APIKey}
                        ${tool('Octo CLI')}/Octo deploy-release \
                            --project WebDriverLambda \
                            --channel ${env.BRANCH_NAME} \
                            --version 1.0.${env.BUILD_NUMBER} \
                            --deploymenttimeout 01:00:00 \
                            --deployto Testing \
                            --waitfordeployment \
                            --server ${OctopusServer} \
                            --apiKey ${APIKey}
                    """
                }
            }
        }
    }
}
