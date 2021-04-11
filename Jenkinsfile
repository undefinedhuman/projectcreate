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
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}