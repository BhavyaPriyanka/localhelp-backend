pipeline{

    agent{
        label 'AGENT-1'
    }

    environment {
        APP_VERSION = ""
    }

    options{
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        ansiColor('xterm')
    }

    

    stages{

        stage('Install Dependencies')
        {

            steps{
                    sh """
                    mvn dependency:resolve
                    ls -la ~/.m2
                    
                    """
            }
        }

    stage('Get Version') {
        steps {
            sh '''
            pwd
            ls -la
            mvn -version
            cat pom.xml
            mvn help:evaluate -Dexpression=project.version -DforceStdout
            '''
        }
}

    stage('Debug') {
        steps {
            script {
                echo "Version from env = ${env.APP_VERSION}"

                sh '''
                echo "Shell sees APP_VERSION=$APP_VERSION"
                '''
            }
        }
}
             }

    post{

        always{

                echo "==== POST SECTION ===== "
                deleteDir()

            }

            success{

                echo "PIPELINE SUCCESSFULL.."
            }

            failure{

                echo "PIPELINE FAILURE.."
            }


    }


}