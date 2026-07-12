pipeline{

    agent{
        label 'AGENT-1'
    }

   

    options{
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        ansiColor('xterm')
    }

    environment{
        def pom = ''
        def version = ''
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
                // Uisng mvn getting version number
                    // script{
                    //         def output = sh(
                    //             script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                    //             returnStdout: true
                    //         ).trim()


                    //          echo "Application Version: ${output}"

                    // }

                    // using plugin getting version number

                    script {
                        pom = readMavenPom file: 'pom.xml'
                        version = pom.version
                        echo "Version using plugin = ${pom.version}"
}
            }
        } 

        stage('Build'){
            steps{

                sh """
                    echo "====== ZIPPING THE CODE + DEPENDENCIES ========"
            zip -q -r localhelp-backend-${version}.zip * -x Jenkinsfile -x localhelp-backend-${version}.zip
            ls -ltr
                """
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