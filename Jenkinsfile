pipeline{

    agent{
        label 'AGENT-1'
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
                            def output = sh(
                                script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                                returnStdout: true
                            ).trim()


                             echo "Application Version: ${output}"

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