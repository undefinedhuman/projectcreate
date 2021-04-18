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
                    env.BY = VersionNumber(versionNumberString: '${BUILD_DATE_FORMATTED,"yy"}')
                    env.BW = VersionNumber(versionNumberString: '${BUILD_WEEK,XX}')
                    env.BTW = VersionNumber(versionNumberString: '${BUILDS_THIS_WEEK}')
                    env.SNAPSHOT = "${env.BY}w${env.BW}b${env.BTW}"
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
                    gradlew(":desktop:dist")
                    deployFile("desktop/build/libs/", "game.jar", "game/", "${env.SNAPSHOT}.jar")
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

def deployFile(String sourceDir, String sourceFileName, String destinationDir, String destinationFileName) {
    def sourceFilePath = "${sourceDir}${sourceFileName}"
    def destinationDuringUploadName = "UPLOAD-${destinationFileName}"
    fileOperations([fileCreateOperation(fileName: "${sourceDir}${destinationDuringUploadName}", fileContent: '')])
    fileOperations([fileRenameOperation(destination: "${sourceDir}${destinationDuringUploadName}", source: "${sourceFilePath}")])
    sshPublisher(
            publishers: [
                    sshPublisherDesc(
                            configName: 'Jenkins Deploy',
                            transfers: [
                                    sshTransfer(
                                            remoteDirectory: "/${destinationDir}",
                                            remoteDirectorySDF: false,
                                            removePrefix: "${sourceDir}",
                                            sourceFiles: "${sourceDir}${destinationDuringUploadName}",
                                            execCommand: "mv ${destinationDir}/${destinationDuringUploadName} ${destinationDir}/${destinationFileName}"),
                            ],
                            verbose: false)
            ]
    )
    fileOperations([fileDeleteOperation(includes: "${sourceDir}${destinationDuringUploadName}")])
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