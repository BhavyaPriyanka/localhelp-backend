pipeline{

    agent{
        label 'AGENT-1'
    }

    environment {
        APP_VER = ""
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

       stage('Get the version number')
        {

            steps{
                   script {
    String version = sh(
        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
        returnStdout: true
    ).trim()

    echo "version=${version}"

    env.TEST = version.toString()

    echo "env.TEST=${env.TEST}"

    sh 'echo TEST=$TEST'
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