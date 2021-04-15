#!groovy
def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'failed']

pipeline {
    agent any

    options {
        gitLabConnection('Gitlab ProjectCreate')
    }

    stages {
        stage('Compile') {
            steps {
                echo BRANCH_NAME
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
        stage('Run Tests') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        updateGitlabCommitStatus name: 'Unit Tests', state: STATUS_MAP[currentBuild.currentResult]
                        gradlew("test")
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: '**/test-results/**/*.xml'
                            updateGitlabCommitStatus name: 'Unit Tests', state: STATUS_MAP[currentBuild.currentResult]
                        }
                    }
                }
                stage('Integration Tests') {
                    steps {
                        updateGitlabCommitStatus name: 'Integration Tests', state: STATUS_MAP[currentBuild.currentResult]
                        gradlew("integrationTest")
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: '**/test-results/**/*.xml'
                            updateGitlabCommitStatus name: 'Integration Tests', state: STATUS_MAP[currentBuild.currentResult]
                        }
                    }
                }
            }
            post {
                always {
                    gradlew("combineJaCoCoReports")
                }
            }
        }
        stage('Static Code Analysis') {
            steps {
                updateGitlabCommitStatus name: 'SonarQube analysis', state: 'pending'
                withSonarQubeEnv('SonarQube ProjectCreate') {
                    gradlew("sonarqube",
                            "-Dsonar.analysis.buildNumber=${currentBuild.number}",
                            "-Dsonar.projectKey=project-create",
                            "-Dsonar.projectName=ProjectCreate")
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
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    script {
                        def qg = waitForQualityGate()
                        if(qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Quality Gate', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Snapshot release') {
            when { expression { GIT_BRANCH == 'origin/dev' } }
            steps {
                script {
                    echo 'STAGING! WOOHOO!'
                }
            }
        }
        stage('') {
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