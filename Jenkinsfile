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

        stage('test')
        {

            steps{
                    echo "testing"
            }
        }
             }

    post{

        always{

                echo "Hello...check status below !!"
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