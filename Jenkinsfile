def pom
def version
def artifactId
def groupId

pipeline {

    agent {
        label 'AGENT-1'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        ansiColor('xterm')
    }

    environment {
        nexusUrl = 'nexus.localhelp.store:8081'
    }

    stages {

        stage('Install Dependencies') {
            steps {
                sh '''
                    mvn dependency:resolve
                    ls -la ~/.m2
                '''
            }
        }

        stage('Read Maven Information') {
            steps {
                script {
                    pom = readMavenPom file: 'pom.xml'

                    version = pom.version
                    artifactId = pom.artifactId
                    groupId = pom.groupId

                    echo "Group Id    : ${groupId}"
                    echo "Artifact Id : ${artifactId}"
                    echo "Version     : ${version}"
                }
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "===== BUILDING APPLICATION ====="
                    mvn clean package -DskipTests

                    echo "===== GENERATED ARTIFACT ====="
                    ls -ltr target
                '''
            }
        }

        stage('Upload Artifact to Nexus') {
            steps {
                script {

                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: nexusUrl,
                        repository: 'backend',
                        credentialsId: 'nexus-auth',

                        groupId: groupId,
                        version: version,

                        artifacts: [
                            [
                                artifactId: artifactId,
                                classifier: '',
                                file: "target/${artifactId}-${version}.jar",
                                type: 'jar'
                            ]
                        ]
                    )

                }
            }
        }

        stage('Trigger Deploy Job'){
            steps{
                build(
                    job: 'BACKEND-DEPLOY',
                    parameters: [
                        string(name: 'VERSION', value: '${version}', wait: false)
                    ]
                )

            }
        }
    }

    post {

        always {
            echo "===== CLEANING WORKSPACE ====="
            deleteDir()
        }

        success {
            echo "Pipeline completed successfully."
        }

        failure {
            echo "Pipeline failed."
        }
    }
}