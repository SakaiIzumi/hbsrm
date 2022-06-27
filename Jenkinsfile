 /* groovylint-disable-next-line CompileStatic */

pipeline {
    agent any
    parameters {
        choice(name: 'app_name',
        choices: [
         'zc_order',
         'zc_contract',
         'delivery',
         'zc_information',
         'zy_contract',
         'zy_order',
         'event_center',
         'logging',
         'financial',
         'bis',
         'file_center',
         'platform',
         'gateway',
         'uaa',
         'quotation',
         'oem',
         'ALL'], description: '选择部署服务')

        choice(name: 'BRANCH_NAME',choices:['dev','uat','prod'], description: '请选择部署分支')

        password(name: 'pwd_srm_test', defaultValue: 'SECRET', description: 'A secret password')
        password(name: 'pwd_srm_prod', defaultValue: 'SECRET', description: 'A secret password')
    }
     environment {
         PROJECT_SOURCE_URL='http://gitlab.bncloud.net/be/bncloud.git'
         CREDENTIALS_ID='bnc-gitlab'
    }

    stages {
        stage('pull code') {
            steps {
                echo "==========================> 1.拉取项目分支"
                echo "==========================> 当前分支: ${BRANCH_NAME}"
                checkout([$class: 'GitSCM', branches: [[name: "${BRANCH_NAME}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${CREDENTIALS_ID}", url: "${PROJECT_SOURCE_URL}"]]])
            }
        }

        stage('build') {
            steps {
                echo "==========================> 2.构建项目"
                sh 'mvn -Dmaven.test.skip=true clean compile package'
            }
        }

        stage('upload'){
           steps {
               script {
                   echo "==========================> 3.上传项目"
                   echo "当前分支: ${BRANCH_NAME}"
                   def BRANCH_NAME="${BRANCH_NAME}"
                   def projectList = getProjectList()
                   for (projectItem in projectList){
                        if("${app_name}" != 'ALL' && "${app_name}" != "${projectItem.app_name}"){
                            continue
                        }else {
                         echo "正在上传: ${projectItem.app_name}"
                         def remoteTargetDir = getRemoteTargetDir()
                         sshPut remote: remote, from: "${projectItem.jar_source_path}", into: "${remoteTargetDir}"
                   }
                }
               }
            }
        }

        stage('deploy'){
           steps {
               script {
                   echo "==========================> 4.部署项目"
                   echo "当前分支: ${BRANCH_NAME}"
                   def BRANCH_NAME="${BRANCH_NAME}"
                   def projectList = getProjectList()
                   for (projectItem in projectList){
                        if("${app_name}" != 'ALL' && "${app_name}" != "${projectItem.app_name}"){
                            continue
                        }else {
                         echo "正在部署: ${projectItem.app_name}"
                         def remoteTargetDir = getRemoteTargetDir()
                         sshCommand remote: remote, command: "cd ${remoteTargetDir} && bash ${projectItem.run_script} restart"
                   }
                }
               }
            }
        }
    }
}


def getProjectList(){
    def platform = [app_name:'platform',jar_source_path:'./modules/platform/target/platform-1.0.0-SNAPSHOT.jar',run_script:'platform-server.sh']
    def gateway = [app_name:'gateway',jar_source_path:'./gateway/target/gateway-1.0.0-SNAPSHOT.jar',run_script:'gateway-server.sh']
    def uaa = [app_name:'uaa',jar_source_path:'./uaa/target/uaa-1.0.0-SNAPSHOT.jar',run_script:'uaa-server.sh']
    def zc_order = [app_name:'zc_order',jar_source_path:'./modules/zc-order/target/zc-order-1.0.0-SNAPSHOT.jar',run_script:'zc-order-server.sh']
    def zy_order = [app_name:'zy_order',jar_source_path:'./modules/zy-order/target/zy-order-1.0.0-SNAPSHOT.jar',run_script:'zy-order-server.sh']
    def zc_contract = [app_name:'zc_contract',jar_source_path:'./modules/zc-contract/target/zc-contract-1.0.0-SNAPSHOT.jar',run_script:'zc-contract-server.sh']
    def zy_contract = [app_name:'zy_contract',jar_source_path:'./modules/zy-contract/target/zy-contract-1.0.0-SNAPSHOT.jar',run_script:'zy-contract-server.sh']
    def delivery = [app_name:'delivery',jar_source_path:'./modules/delivery/target/delivery-1.0.0-SNAPSHOT.jar',run_script:'delivery-server.sh']
    def zc_information = [app_name:'zc_information',jar_source_path:'./modules/zc-information/target/zc-information-1.0.0-SNAPSHOT.jar',run_script:'zc-information-server.sh']
    def event_center = [app_name:'event_center',jar_source_path:'./modules/event-center/target/event-center-1.0.0-SNAPSHOT.jar',run_script:'event-center-server.sh']
    def file_center = [app_name:'file_center',jar_source_path:'./modules/file-center/target/file-center-1.0.0-SNAPSHOT.jar',run_script:'event-center-server.sh']
    def bis = [app_name:'bis',jar_source_path:'./modules/bis/target/bis-1.0.0-SNAPSHOT.jar',run_script:'bis-server.sh']
    def logging = [app_name:'logging',jar_source_path:'./modules/logging/target/logging-1.0.0-SNAPSHOT.jar',run_script:'logging-server.sh']
    def financial = [app_name:'financial',jar_source_path:'./modules/financial/target/financial-1.0.0-SNAPSHOT.jar',run_script:'financial-server.sh']
    def quotation = [app_name:'quotation',jar_source_path:'./modules/quotation/target/quotation-1.0.0-SNAPSHOT.jar',run_script:'quotation-server.sh']
    def oem = [app_name:'oem',jar_source_path:'./modules/oem/target/oem-1.0.0-SNAPSHOT.jar',run_script:'oem-server.sh']

    def projectList = [platform,gateway,uaa,zc_order,zy_order,zc_contract,zy_contract,delivery,zc_information,event_center,file_center,bis,logging,financial,quotation,oem]
    return projectList
}

def getRemoteTargetDir(){
    def BRANCH_NAME="${BRANCH_NAME}"
    def remoteTargetDir=""
     switch (BRANCH_NAME) {
            case "dev":
               remoteTargetDir="/home/app/run"
                break;
            case "uat":
               remoteTargetDir="/home/srm-test/run"
               break;
            case "prod":
               remoteTargetDir="/srm/run"
               break;
     }
    return remoteTargetDir
}

def getRemote(){
    def BRANCH_NAME="${BRANCH_NAME}"
    def host = ""
    def user = ""
    def password=""
    switch (BRANCH_NAME) {
        case "dev":
            host="192.168.2.136"
            user="app"
            password="Aa931215"
            break;

        case "uat":
           host="52.131.40.199"
           user="srm-test"
           password="${pwd_srm_test}"
           break;
        case "prod":
           host="52.131.35.100"
           user="srm-prod"
           password="${pwd_srm_prod}"
           break;
     }
    def remote = [:]
    remote.name = "bncloud"
    remote.host = "${host}"
    remote.user = "${user}"
    remote.password = "${password}"
    remote.allowAnyHosts = true
    return remote
}
