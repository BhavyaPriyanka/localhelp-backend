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
           script {

    def output = sh(
        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
        returnStdout: true
    )

    echo "Raw output: >>>${output}<<<"

    env.APP_VERSION = output.trim()

    echo "APP_VERSION: >>>${env.APP_VERSION}<<<"

    env.NAME = "ABC123"
    echo "NAME = ${env.NAME}"
}
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