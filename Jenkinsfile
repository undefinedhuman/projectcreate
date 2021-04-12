pipeline {
    agent any
    stages {
        stage('Compile') {
            steps {
                updateGitlabCommitStatus name: 'Compile', state: 'pending'
                gradlew('clean')
                updateGitlabCommitStatus name: 'Compile', state: 'success'
            }
        }
        stage('SonarQube analysis') {
            steps {
                updateGitlabCommitStatus name: 'SonarQube analysis', state: 'pending'
                withSonarQubeEnv('SonarQube ProjectCreate') {
                    gradlew("sonarqube")
                }
                updateGitlabCommitStatus name: 'SonarQube analysis', state: 'success'
            }
        }
        stage("Quality Gate") {
            steps {
                updateGitlabCommitStatus name: 'Quality Gate', state: 'pending'
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
                updateGitlabCommitStatus name: 'Quality Gate', state: 'success'
            }
        }
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}