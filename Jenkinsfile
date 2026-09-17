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
    }

    environment {
        nexusUrl = 'nexus.localhelp.store:8081'
        account_id = '837206354502'
        region = 'us-east-1'
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
            version = sh(
                script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                returnStdout: true
            ).trim()
            artifactId = sh(
                script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout",
                returnStdout: true
            ).trim()
            groupId = sh(
                script: "mvn help:evaluate -Dexpression=project.groupId -q -DforceStdout",
                returnStdout: true
            ).trim()
            echo "Group Id    : ${groupId}"
            echo "Artifact Id : ${artifactId}"
            echo "Version     : ${version}"
        }
    }
}

      stage('Build') {
    steps {
        script {
            def version = sh(
                script: "git describe --tags --exact-match 2>/dev/null || true",
                returnStdout: true
            ).trim()

            if (!version) {
                error("Build must be triggered from a Git tag, e.g. v1.9.0")
            }

            env.VERSION = version.replaceFirst(/^v/, '')

            echo "===== RELEASE VERSION: ${env.VERSION} ====="
        }

        sh '''
            echo "===== BUILDING APPLICATION ====="

            mvn -q clean package -DskipTests

            echo "===== GENERATED ARTIFACT ====="
            ls -ltr target
        '''
    }
}

        stage('Docker Build and Push to ECR') {
    steps {
        sh """
            echo "===== LOGIN TO ECR ====="

            aws ecr get-login-password --region ${region} | \
            docker login --username AWS --password-stdin \
            ${account_id}.dkr.ecr.${region}.amazonaws.com

            echo "===== BUILDING DOCKER IMAGE ====="

            docker build \
              -t ${account_id}.dkr.ecr.${region}.amazonaws.com/localhelp-backend:${VERSION} .

            echo "===== DOCKER IMAGE CREATED ====="

            docker images | grep localhelp-backend

            echo "===== PUSHING IMAGE TO ECR ====="

            docker push \
              ${account_id}.dkr.ecr.${region}.amazonaws.com/localhelp-backend:${VERSION}
        """
    }
}


        stage('Deploy to K8') {
    steps {
        sh """
            set -e

            echo "========= Authenticate to K8 ============"

            aws eks update-kubeconfig \
              --region us-east-1 \
              --name localhelp-dev

            export KUBECONFIG=/home/ec2-user/.kube/config

            echo "========= Check Kubernetes Nodes =========="

            kubectl get nodes

            echo "========= Deploy Backend using Helm =========="

            cd helm

            sed -i 's/IMAGE_VERSION/${version}/g' values.yaml

           

            echo "========= Helm Upgrade / Install =========="

            helm upgrade --install backend . \
              --namespace localhelp \
              --create-namespace

            echo "========= Helm Release =========="

            helm status backend \
              --namespace localhelp

            echo "========= CHECK NAMESPACE =========="

            kubectl get ns

            echo "========= CHECK DEPLOYMENT =========="

            kubectl get deployment backend \
              -n localhelp

            echo "========= CHECK PODS =========="

            kubectl get pods \
              -n localhelp \
              -o wide

            echo "========= WAIT FOR ROLLOUT =========="

            kubectl rollout status deployment/backend \
              -n localhelp \
              --timeout=180s

            echo "========= FINAL POD STATUS =========="

            kubectl get pods \
              -n localhelp \
              -o wide

            echo "========= BACKEND SERVICE =========="

            kubectl get svc backend \
              -n localhelp

            echo "========= DEPLOYMENT COMPLETE =========="
        """
    }
}
//         stage('SonarQube Analysis') {
//     steps {
//       script {

//             withSonarQubeEnv('sonarqube') {

//                 sh """
//                     echo "===== SONARQUBE ANALYSIS ====="

//                     mvn sonar:sonar \
//                     -Dsonar.projectKey=${artifactId} \
//                     -Dsonar.projectName=${artifactId} \
//                     -Dsonar.host.url=http://sonar.localhelp.store:9000

//                 """

//             }

//         }

//     }
// }

        // stage('Quality Gate') {
        //     steps {
        //         timeout(time: 5, unit: 'MINUTES') {
        //             script {
        //                 def qg = waitForQualityGate()

        //                 if (qg.status != 'OK') {
        //                     error "Pipeline aborted because Quality Gate failed: ${qg.status}"
        //                 }

        //                 echo "Quality Gate Passed."
        //             }
        //         }
        //     }
        // }

        //  stage('Dependency Scan') {
        //         steps {
        //             sh '''
        //            trivy fs \
        //             --scanners vuln \
        //             --severity HIGH,CRITICAL \
        //             --exit-code 1 \
        //             --skip-dirs target \
        //             .
                                        
        //             '''
        //         }
        //     }


        // stage('Test Nexus Credential') {
        //         steps {
        //             withCredentials([
        //                 usernamePassword(
        //                     credentialsId: 'nexus-auth',
        //                     usernameVariable: 'USER',
        //                     passwordVariable: 'PASS'
        //                 )
        //             ]) {
        //                 sh '''
        //                     echo "Nexus User: $USER"

        //                     curl -v -u "$USER:$PASS" \
        //                     http://nexus.localhelp.store:8081/service/rest/v1/status
        //                 '''
        //             }
        //         }
        //     }
        // stage('Upload Artifact to Nexus') {
        //     steps {
        //         script {

        //             nexusArtifactUploader(
        //                 nexusVersion: 'nexus3',
        //                 protocol: 'http',
        //                 nexusUrl: nexusUrl,
        //                 repository: 'backend',
        //                 credentialsId: 'nexus-auth',

        //                 groupId: groupId,
        //                 version: version,

        //                 artifacts: [
        //                     [
        //                         artifactId: artifactId,
        //                         classifier: '',
        //                         file: "target/${artifactId}-${version}.jar",
        //                         type: 'jar'
        //                     ],
        //                     [
        //                         artifactId: artifactId,
        //                         classifier: 'db',
        //                         file: "db/init.sql",
        //                         type: 'sql'
        //                     ]
        //                 ]
        //             )

        //         }
        //     }
        // }

                stage('Upload Artifact to S3') {
                    steps {
                        sh """
                            echo "===== UPLOADING ARTIFACTS TO S3 ====="

                            aws s3 cp \
                                target/${artifactId}-${version}.jar \
                                s3://localhelp-backend-artifacts/backend/${version}/${artifactId}-${version}.jar

                            aws s3 cp \
                                db/init.sql \
                                s3://localhelp-backend-artifacts/backend/${version}/init.sql

                            echo "===== S3 UPLOAD COMPLETED ====="

                            echo "===== S3 ARTIFACTS ====="
                            aws s3 ls s3://localhelp-backend-artifacts/backend/${version}/
                        """
                    }
        }

        // stage('Trigger Deploy Job'){
        //     steps{
        //         build(
        //             job: 'backend-deploy',
        //             wait: false,
        //             parameters: [
        //                 string(name: 'VERSION', value: version)
        //             ]
        //         )

        //     }
        // }
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