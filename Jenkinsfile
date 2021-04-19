#!groovy
def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'failed']

pipeline {
    agent any

    options {
        gitLabConnection('ProjectCreate Gitlab')
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
            when { expression { BRANCH_NAME == "snapshot" } }
            steps {
                script {
                    gradlew(":game:dist")
                    deployFile("game", "versions/", "snapshot-${env.SNAPSHOT}.jar")
                }
            }
        }
        stage('Deploy release candidate') {
            when { expression { BRANCH_NAME ==~ '^(indev|alpha|beta|release)-(game|launcher|updater|editor)-[0-9]+.[0-9]+.[0-9]+' } }
            steps {
                script {
                    // CHANGE BUILDS ALL TIME TO ONLY CHECK IF BELOW SUCCED UNTIL DOWNLOAD TO NOT COUNT FAILED PIPELINES OR LOOK DOC

                    def versionString = "${BRANCH_NAME}".split("-")
                    echo versionString[0] + ", " + versionString[1] + ", " + versionString[2]
                    if(versionString.size() != 3)
                        error('Release branch name does not follow the required scheme [indev, alpha, beta, release]-[game, launcher, updater, server]-STAGE.MAJOR.MINOR')
                    def stage = versionString[0] as String
                    def module = versionString[1] as String
                    def version = versionString[2] as String
                    def versionNumber = VersionNumber(versionNumberString: '${BUILDS_ALL_TIME}', skipFailedBuilds: true) as String
                    gradlew(":game:dist")
                    deployFile("${module}", "${module}/", "${stage}-${version}-rc${versionNumber}.jar")
                    if(module.equalsIgnoreCase("game")) {
                        gradlew(":server:dist")
                        deployFile("server", "server/", "${stage}-${version}-rc${versionNumber}.jar")
                    }
                }
            }
        }
    }
}

def deployFile(String sourceName, String destinationDir, String destinationFileName) {
    def destinationDuringUploadName = "UPLOAD-${destinationFileName}"
    def sourceDir = "${sourceName}/build/libs/"
    fileOperations([fileRenameOperation(source: "${sourceDir}${sourceName}.jar", destination: "${sourceDir}${destinationDuringUploadName}", )])
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
                                            execCommand: "mv ${destinationDir}${destinationDuringUploadName} ${destinationDir}${destinationFileName}"),
                            ],
                            verbose: false)
            ]
    )
    fileOperations([fileDeleteOperation(includes: "${sourceDir}${destinationDuringUploadName}")])
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}