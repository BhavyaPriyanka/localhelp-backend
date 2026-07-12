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

       stage('Get the version number')
        {

            steps{
                    script{
                            def version = sh(
                                script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                                returnStdout: true
                            )

                                   

                                    env.APP_VERSION = version.trim().toString()

                             echo "Application Version: ${env.APP_VERSION}"

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