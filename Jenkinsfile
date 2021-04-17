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
        stage('Build') {
            steps {
                updateGitlabCommitStatus name: 'Build', state: 'pending'
                script {
                    def calendar = Calendar.getInstance(Locale.GERMANY)
                    env.BY = VersionNumber(versionNumberString: '${BUILD_DATE_FORMATTED,"yy"}')
                    env.BW = calendar.get(Calendar.WEEK_OF_YEAR)
                    env.BTW = VersionNumber(versionNumberString: '${BUILDS_THIS_WEEK}')
                }
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Build', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Unit Tests') {
            steps {
                updateGitlabCommitStatus name: 'Unit Tests', state: STATUS_MAP[currentBuild.currentResult]
                gradlew("test")
                stash allowEmpty: true, includes: '**/unitReports/*.xml', name: 'unitTestReports'
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Unit Tests', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Integration Tests') {
            steps {
                updateGitlabCommitStatus name: 'Integration Tests', state: STATUS_MAP[currentBuild.currentResult]
                gradlew("integrationTest")
                stash allowEmpty: true, includes: '**/integrationReports/*.xml', name: 'integrationTestReports'
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Integration Tests', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Reporting') {
            steps {
                unstash 'unitTestReports'
                unstash 'integrationTestReports'
                junit allowEmptyResults: true, testResults: '**/TEST-*.xml'
                gradlew("jacocoTestReport")
                stash includes: '**/jacocoTestReport.xml', name: 'jacocoReports'
            }
        }

        stage('Static Code Analysis') {
            tools {
                jdk "openjdk-11"
            }
            steps {
                updateGitlabCommitStatus name: 'SonarQube analysis', state: 'pending'
                unstash 'jacocoReports'
                withSonarQubeEnv('SonarQube ProjectCreate') {
                    gradlew("sonarqube",
                            "-Dsonar.analysis.buildNumber=${currentBuild.number}",
                            "-Dsonar.projectKey=project-create",
                            "-Dsonar.projectName=ProjectCreate",
                            "-Dsonar.java.coveragePlugin=jacoco")
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
                }
            }
            post {
                always {
                    updateGitlabCommitStatus name: 'Quality Gate', state: STATUS_MAP[currentBuild.currentResult]
                }
            }
        }
        stage('Deploy snapshot') {
            when { expression { BRANCH_NAME == "dev" } }
            steps {
                script {
                    echo "${env.BY}w${env.BW}b${env.BTW}"
                    gradlew(":desktop:dist")
                    sshPublisher(
                            publishers: [
                            sshPublisherDesc(
                                    configName: 'Jenkins Deploy',
                                    transfers: [
                                            sshTransfer(
                                                    cleanRemote: false,
                                                    excludes: '',
                                                    execCommand: 'apt-get update',
                                                    execTimeout: 120000,
                                                    flatten: false,
                                                    makeEmptyDirs: false,
                                                    noDefaultExcludes: false,
                                                    patternSeparator: '[, ]+',
                                                    remoteDirectory: '/game/',
                                                    remoteDirectorySDF: false,
                                                    removePrefix: 'desktop/build/libs/',
                                                    sourceFiles: 'desktop/build/libs/game.jar')
                                    ],
                                    usePromotionTimestamp: false,
                                    useWorkspaceInPromotion: false,
                                    verbose: false)
                            ]
                    )
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