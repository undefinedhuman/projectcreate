#!groovy
def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'failed']

pipeline {
    agent any
    stages {
        stage('Compile') {
            steps {
                updateGitlabCommitStatus name: 'Compile', state: 'pending'
                gradlew('clean')
                gradlew('compileJava')
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Compile', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Static Code Analysis') {
            steps {
                updateGitlabCommitStatus name: 'SonarQube analysis', state: 'pending'
                withSonarQubeEnv('SonarQube ProjectCreate') {
                    gradlew("sonarqube",
                            "-Dsonar.analysis.buildNumber=${currentBuild.number}",
                            "-Dsonar.projectKey=project-create-${getGitBranchName("${GIT_BRANCH}")}",
                            "-Dsonar.projectName=ProjectCreate-${getGitBranchName("${GIT_BRANCH}")}")
                }
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'SonarQube analysis', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage("Quality Gate") {
            steps {
                updateGitlabCommitStatus name: 'Quality Gate', state: 'pending'
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Quality Gate', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Staging') {
            when {
                expression {
                    GIT_BRANCH == 'origin/dev'
                }
            }
            steps {
                script {
                    echo 'STAGING! WOOHOO!'
                }
            }
        }
        stage('Deploy') {
            when {
                expression {
                    GIT_BRANCH == 'origin/main'
                }
            }
            steps {
                script {
                    echo 'DEPLOY! WOOHOO!'
                }
            }
        }
    }
}

static String getGitBranchName(String gitBranch) {
    def names = gitBranch.split("/", 2)
    if(names.size() > 1)
        return "${names[1].replaceAll("/", "-")}"
    return ""
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}