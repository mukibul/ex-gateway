#!groovy​

properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

stage('build') {
            node {
                checkout scm
                def v = version()
                currentBuild.displayName = "${env.BRANCH_NAME}-${v}-${env.BUILD_NUMBER}"
                mvn "clean verify"
            }
        }

def branch_type = get_branch_type "${env.BRANCH_NAME}"
def branch_deployment_environment = get_branch_deployment_environment branch_type

if (branch_deployment_environment) {
    stage('deploy') {
        if (branch_deployment_environment == "prod") {

        }

        if (branch_deployment_environment != "prod") {

        }
    }
}

if (branch_type == "dev") {
    stage('start release') {
        node {
            sshagent(['ssh-config']) {
                def commit = sh returnStdout: true, script: 'git show'
                def canPerformRelease = commit.contains("'feature/")
                echo "Can perform release = ${canPerformRelease}"
                def snapshotVersion = version()
                if (commit.contains("feature")) {

                    //Trigger job to build docker image for dev environment (SNAPSHOT) and push image to nexus
                    build job: 'ssp-gateway-dev-deploy'

                    //update dev sandbox with snapshot version
                    node {
                        sh "rm -rf sandbox"
                        sh "git clone git@git.swblr.skidata.net:ssp/ssp-dev-sandbox.git ./sandbox"

                        //update image version in docker-compose.yml
                        sh "sed -i -e \"s/\\(image: swblr.skidata.net\\/ssp-gateway\\).*/\\1:${snapshotVersion}/\" sandbox/docker-compose.yml"

                        sh "cd sandbox && git add ."

                        //push changes back to git
                        def gitStatus = sh returnStdout: true, script: 'cd sandbox && git status'
                        if(gitStatus.contains("Changes to be committed")) {
                            sh "cd sandbox && git commit -am \"Updating image version for ssp-gateway\""
                            sh "cd sandbox && git push origin master"
                            echo 'Push successful'
                        }
                        sh "rm -rf sandbox"
                    }

                    //trigger job to restart the service in dev environment with latest snapshot version
                    build job: 'ssp-gateway-dev-run'
                }
            }
        }
    }
}

if (branch_type == "hotfix") {
    stage('finish hotfix') {
        timeout(time: 1, unit: 'HOURS') {
            input "Is the hotfix finished?"
        }
        node {
            sshagent(['ssh-config']) {
                mvn("jgitflow:hotfix-finish -Dmaven.javadoc.skip=true -DnoDeploy=true")
            }
        }
    }
}

// Utility functions
def get_branch_type(String branch_name) {
    //Must be specified according to <flowInitContext> configuration of jgitflow-maven-plugin in pom.xml
    def dev_pattern = ".*development"
    def release_pattern = ".*release/.*"
    def feature_pattern = ".*feature/.*"
    def hotfix_pattern = ".*hotfix/.*"
    def master_pattern = ".*master"
    if (branch_name =~ dev_pattern) {
        return "dev"
    } else if (branch_name =~ release_pattern) {
        return "release"
    } else if (branch_name =~ master_pattern) {
        return "master"
    } else if (branch_name =~ feature_pattern) {
        return "feature"
    } else if (branch_name =~ hotfix_pattern) {
        return "hotfix"
    } else {
        return null;
    }
}

def get_branch_deployment_environment(String branch_type) {
    if (branch_type == "dev") {
        return "dev"
    } else if (branch_type == "release") {
        return "staging"
    } else if (branch_type == "master") {
        return "prod"
    } else {
        return null;
    }
}

def mvn(String goals) {
    def mvnHome = tool "maven-3.3.9"
    def javaHome = tool "openjdk-8"

    withEnv(["JAVA_HOME=${javaHome}", "PATH+MAVEN=${mvnHome}/bin"]) {
        sh "mvn -B ${goals}"
    }
}

def version() {
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    return matcher ? matcher[0][1] : null
}