pipeline {
    agent any

    options {
        timeout(time: 180, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
        skipDefaultCheckout true
    }

    stages {
        stage('Dependency check') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                script {
                    sh """
                        mvn org.owasp:dependency-check-maven:check -Dformats='HTML,XML,JSON'  -DfailOnError=false
                    """
                    zip archive: true, glob: 'target/dependency-check-report*.*', zipFile: "dependency-check-reports.zip";
                    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                }//script
            }//steps
        }//stage
        stage('Dependency track') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                script {
                    sh """
                        mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom -DincludeTestScope=true -DfailOnError=false
                    """
                }//script
            }//steps
        }//stage
    }
}