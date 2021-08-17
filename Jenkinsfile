pipeline{
    agent any
    environment
            {
                VERSION = 'latest'
                PROJECT = 'keyvaluestore-service'
                IMAGE = 'keyvaluestore-service'
                LATEST = 'keyvaluestore-service:latest'
                ECRURL = '188367751657.dkr.ecr.ap-south-1.amazonaws.com/$LATEST'
                CONTEXT_PATH = '/keyvalue-service'
            }
    stages {
        stage('Checkout Source') {
            steps {
                git url:'https://github.com/devdream02/keyvaluestore.git', branch:'master'
            }
        }
        stage('Code-coverage') {
            steps
                    {
                        echo "Computing code coverage"
                        sh '''
                        mvn clean clover:setup test clover:aggregate clover:clover -Dmaven.test.failure.ignore=true
                        '''
                        publishHTML target: [
                                allowMissing: false,
                                alwaysLinkToLastBuild: false,
                                keepAll: true,
                                reportDir: 'target/site/clover',
                                reportFiles: 'index.html',
                                reportName: 'Code Covergae Report'
                        ]
                    }
        }
        stage('Docker build')
                {
                    steps
                            {
                                script
                                        {
                                            // Build the docker image using a Dockerfile
                                            sh("docker build -t $IMAGE .")
                                            sh("docker tag $LATEST $ECRURL")
                                        }
                            }
                }
        stage('Create repository on AWS ECR') {
            steps {
                script {
                        sh'''
                          export AWS_DEFAULT_REGION="ap-south-1"
                          ecrDetails=$(aws ecr create-repository --repository-name ${PROJECT}) || true
                        '''
                }
            }
        }
        stage('Docker push')
                {
                    steps
                            {
                                script
                                        {
                                            // login to ECR // Push the Docker image to ECR
                                            sh("eval \$(aws ecr get-login --no-include-email --region ap-south-1) && docker push $ECRURL" )

                                        }
                            }
                }
        stage ('Deploying to Dev Environment')
                {
                    steps
                        {
                            script
                                    {
                                            //deploying build to kubernetes
                                            echo "Deployment started"
                                            sh '''
                                            tempCred=$(aws sts assume-role --role-arn <EKS role arn> --role-session-name eks)
                                            echo $tempCred
                                            access_key=`echo $tempCred | jq -r .Credentials.AccessKeyId`
                                            secret_key=`echo $tempCred | jq -r .Credentials.SecretAccessKey`
                                            session=`echo $tempCred | jq -r .Credentials.SessionToken`
                                            export AWS_ACCESS_KEY_ID=${access_key}
                                            export AWS_SECRET_ACCESS_KEY=${secret_key}
                                            export AWS_SESSION_TOKEN=${session}
                                            export AWS_DEFAULT_REGION="ap-south-1"
                                            aws sts get-caller-identity
                                            aws eks --region ap-south-1 update-kubeconfig --name EKC-KYC
                                            latest=$LATEST
                                            kubectl delete deployment $PROJECT || true
                                            kubectl delete svc $PROJECT || true
                                            kubectl run $PROJECT --overrides='{"spec":{"template":{ "spec":{"containers":[{"name":"'${PROJECT}'","image":"'$IMAGE'","resources":{"requests":{"memory":"350Mi","cpu":"50m"},"limits":{"memory":"500Mi","cpu":"200m"}},"readinessProbe": { "httpGet": { "path": "/'${CONTEXT_PATH}'/actuator/health/ping", "port": 8080, "scheme": "HTTP" }, "initialDelaySeconds": 180, "timeoutSeconds": 10, "periodSeconds": 10, "successThreshold": 1, "failureThreshold": 5 }}}}}' --replicas=1 --labels=app=${PROJECT} --image=$IMAGE --port=8080
                                            kubectl expose deployment $PROJECT --type=ClusterIP --name=$PROJECT
                                            '''

                                        }
                            }
                }
    }
}