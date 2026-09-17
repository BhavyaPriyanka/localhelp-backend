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

stage('Set Maven Version from Git Tag') {
    steps {
        script {
            def gitTag = sh(
                script: "git describe --tags --exact-match 2>/dev/null || true",
                returnStdout: true
            ).trim()

            if (!gitTag) {
                error("Build must be triggered from a Git tag, e.g. v1.9.0")
            }

            version = gitTag.replaceFirst(/^v/, '')

            echo "===== GIT RELEASE VERSION ====="
            echo "Git Tag : ${gitTag}"
            echo "Version : ${version}"

            sh """
                mvn versions:set \
                  -DnewVersion=${version} \
                  -DgenerateBackupPoms=false
            """

            echo "POM version updated to ${version}"
        }
    }
}

stage('Read Maven Information') {
    steps {
        script {
            artifactId = sh(
                script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout",
                returnStdout: true
            ).trim()

            groupId = sh(
                script: "mvn help:evaluate -Dexpression=project.groupId -q -DforceStdout",
                returnStdout: true
            ).trim()

            echo "Group Id     : ${groupId}"
            echo "Artifact Id  : ${artifactId}"
            echo "Maven Version: ${version}"
        }
    }
}



        stage('Build') {
            steps {
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
                      -t ${account_id}.dkr.ecr.${region}.amazonaws.com/localhelp-backend:${version} .

                    echo "===== DOCKER IMAGE CREATED ====="

                    docker images | grep localhelp-backend

                    echo "===== PUSHING IMAGE TO ECR ====="

                    docker push \
                      ${account_id}.dkr.ecr.${region}.amazonaws.com/localhelp-backend:${version}
                """
            }
        }

        stage('Deploy to K8') {
    steps {
        sh """
            set -e

            echo "========= Copy Helm Chart to Bastion =========="

            rm -rf /tmp/backend-helm

            scp -i /home/ec2-user/.ssh/jenkins_bastion \
              -o StrictHostKeyChecking=no \
              -r helm \
              ec2-user@10.0.1.109:/tmp/backend-helm

            echo "========= Deploy Backend to EKS =========="

            ssh -i /home/ec2-user/.ssh/jenkins_bastion \
              -o StrictHostKeyChecking=no \
              ec2-user@10.0.1.109 \
              "VERSION='${version}' bash -s" <<'REMOTE_SCRIPT'

                set -e

                echo "========= Check Kubernetes Nodes =========="

                kubectl get nodes

                echo "========= Set Image Version =========="

                cd /tmp/backend-helm

                sed -i "s/IMAGE_VERSION/\${VERSION}/g" values.yaml

                echo "========= Helm Upgrade / Install =========="

                helm upgrade --install backend . \\
                  --namespace localhelp \\
                  --create-namespace

                echo "========= Helm Release =========="

                helm status backend \\
                  --namespace localhelp

                echo "========= CHECK NAMESPACE =========="

                kubectl get ns

                echo "========= CHECK DEPLOYMENT =========="

                kubectl get deployment backend \\
                  -n localhelp

                echo "========= CHECK PODS =========="

                kubectl get pods \\
                  -n localhelp \\
                  -o wide

                echo "========= WAIT FOR ROLLOUT =========="

                kubectl rollout status deployment/backend \\
                  -n localhelp \\
                  --timeout=180s

                echo "========= FINAL POD STATUS =========="

                kubectl get pods \\
                  -n localhelp \\
                  -o wide

                echo "========= BACKEND SERVICE =========="

                kubectl get svc backend \\
                  -n localhelp

                echo "========= DEPLOYMENT COMPLETE =========="

REMOTE_SCRIPT
        """
    }
}

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

                    aws s3 ls \
                        s3://localhelp-backend-artifacts/backend/${version}/
                """
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