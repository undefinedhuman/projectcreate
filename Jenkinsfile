#!groovy
def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'failed']
DEPLOY_FILE_EXTENSION = ".jar"

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
                    env.BTW = VersionNumber(versionNumberString: '${BUILDS_THIS_WEEK}', worstResultForIncrement: 'SUCCESS')
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
        stage('Deploy dev server') {
            when { expression { BRANCH_NAME == "dev" } }
            steps {
                script {
                    gradlew(":server:dist" as String)
                    deployToTestServer("dev")
                }
            }
        }
        stage('Deploy snapshot') {
            when { expression { BRANCH_NAME == "snapshot" } }
            steps {
                script {
                    buildAndDeployModule("game", "snapshot-${env.SNAPSHOT}${DEPLOY_FILE_EXTENSION}")
                    buildAndDeployModule("server", "snapshot-${env.SNAPSHOT}${DEPLOY_FILE_EXTENSION}")
                    deployToTestServer("snapshot")
                }
            }
        }
        stage('Deploy release candidate') {
            when { expression { BRANCH_NAME ==~ '^(release/)(indev|alpha|beta|release)-(game|launcher|updater|editor)-[0-9]+.[0-9]+.[0-9]+' } }
            steps {
                script {
                    def versionString = "${BRANCH_NAME}".split("/", 2)[1].split("-")
                    def stage = versionString[0] as String
                    def module = versionString[1] as String
                    def version = versionString[2] as String
                    def versionNumber = VersionNumber(versionNumberString: '${BUILDS_ALL_TIME}', worstResultForIncrement: 'SUCCESS') as String
                    switch(module) {
                        case "game":
                            buildAndDeployModule(module, "${stage}-${version}-rc${versionNumber}${DEPLOY_FILE_EXTENSION}")
                            buildAndDeployModule("server", "${stage}-${version}-rc${versionNumber}${DEPLOY_FILE_EXTENSION}")
                            deployToTestServer("release")
                            break
                        case "updater":
                            gradlew(":updater:dist" as String)
                            deployUpdater("${stage}-${version}-rc${versionNumber}.zip", false)
                            break
                        default:
                            buildAndDeployModule(module, "${stage}-${version}-rc${versionNumber}${DEPLOY_FILE_EXTENSION}")
                    }
                }
            }
        }
        stage('Deploy release') {
            when { expression { BRANCH_NAME == 'main' } }
            steps {
                script {
                    TAG = sh(script: 'git tag --points-at HEAD | awk NF', returnStdout: true).trim()
                    def versionString = "${TAG}".split("-")
                    def stage = versionString[0] as String
                    def module = versionString[1] as String
                    def version = versionString[2] as String
                    switch(module) {
                        case "game":
                            buildAndDeployModule(module, "${stage}-${version}${DEPLOY_FILE_EXTENSION}")
                            buildAndDeployModule("server", "${stage}-${version}${DEPLOY_FILE_EXTENSION}")
                            break
                        case "updater":
                            gradlew(":updater:dist" as String)
                            deployUpdater("${stage}-${version}.zip", true)
                            break
                        default:
                            buildAndDeployModule(module, "${stage}-${version}${DEPLOY_FILE_EXTENSION}")
                    }
                }
            }
        }
    }
}

def deployToTestServer(String stage) {
    sshPublisher(
            failOnError: false,
            publishers: [
                    sshPublisherDesc(
                            configName: "Jenkins Deploy",
                            transfers: [
                                    sshTransfer(execCommand: "sh instances/${stage}/stop.sh")
                            ],
                            verbose: false)
            ]
    )
    sshPublisher(
            failOnError: false,
            publishers: [
                    sshPublisherDesc(
                            configName: "Jenkins Deploy",
                            transfers: [
                                    sshTransfer(execCommand: "rm instances/${stage}/server.jar")
                            ],
                            verbose: false)
            ]
    )
    deployFile("server/build/libs/", "server.jar", "instances/${stage}/", "server.jar")
    sshPublisher(
            failOnError: false,
            publishers: [
                    sshPublisherDesc(
                            configName: "Jenkins Deploy",
                            transfers: [
                                    sshTransfer(execCommand: "cd instances/${stage}/ && ./start.sh"),
                            ],
                            verbose: false)
            ]
    )
}

def deployUpdater(String destinationName, boolean deployLatest) {
    deployUpdaterForOS("windows", destinationName, deployLatest)
    deployUpdaterForOS("linux", destinationName, deployLatest)
    deployUpdaterForOS("macos", destinationName, deployLatest)
}

def deployUpdaterForOS(String os, String destinationName, boolean deployLatest) {
    if(deployLatest) {
        sshPublisher(
                failOnError: false,
                publishers: [
                        sshPublisherDesc(
                                configName: "Jenkins Deploy",
                                transfers: [
                                        sshTransfer(execCommand: "rm updater/${os}/latest.zip"),
                                ],
                                verbose: false)
                ]
        )
    }
    fileOperations([folderDeleteOperation(folderPath: "libs/ProjectCreate")])
    sh "chmod +x -R libs/pack.sh"
    sh "libs/pack.sh -o ${os}"
    fileOperations([fileZipOperation(folderPath: "libs/ProjectCreate", outputFolderPath: "libs" )])
    def zipName = "ProjectCreate.zip"
    deployFile("libs/", zipName, "updater/${os}/", destinationName)
    if(deployLatest) {
        fileOperations([fileZipOperation(folderPath: "libs/ProjectCreate", outputFolderPath: "libs" )])
        deployFile("libs/", zipName, "updater/${os}/", "latest.zip")
    }
    fileOperations([folderDeleteOperation(folderPath: "libs/ProjectCreate")])
}

def buildAndDeployModule(String moduleName, String destinationName) {
    gradlew(":${moduleName}:dist" as String)
    deployFile("${moduleName}/build/libs/", "${moduleName}${DEPLOY_FILE_EXTENSION}", "${moduleName}/", "${destinationName}")
}

def deployFile(String sourceDirectory, String sourceFileName, String destinationDir, String destinationFileName) {
    def destinationDuringUploadName = "UPLOAD-${destinationFileName}"
    fileOperations([fileRenameOperation(source: "${sourceDirectory}${sourceFileName}", destination: "${sourceDirectory}${destinationDuringUploadName}", )])
    sshPublisher(
            publishers: [
                    sshPublisherDesc(
                            configName: "Jenkins Deploy",
                            transfers: [
                                    sshTransfer(
                                            remoteDirectory: "/${destinationDir}",
                                            remoteDirectorySDF: false,
                                            removePrefix: "${sourceDirectory}",
                                            sourceFiles: "${sourceDirectory}${destinationDuringUploadName}",
                                            execCommand: "mv ${destinationDir}${destinationDuringUploadName} ${destinationDir}${destinationFileName}"),
                            ],
                            verbose: false)
            ]
    )
    fileOperations([fileDeleteOperation(includes: "${sourceDirectory}${destinationDuringUploadName}")])
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
