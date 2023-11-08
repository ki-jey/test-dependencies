pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
        skipDefaultCheckout true
    }

    tools {
        maven 'mvn-3.9.5'
    }

    stages {
        stage('Dependency check') {
            options {
                timeout(time: 30, unit: 'MINUTES')
            }
            steps {
//                checkout([
//                        $class: 'GitSCM',
//                        branches: [[name: 'master']],
//                        doGenerateSubmoduleConfigurations: false,
//                        extensions: [[$class: 'LocalBranch']],
//                        userRemoteConfigs: [[url: "https://github.com/ki-jey/test-dependencies.git"]]
//                ])
                checkout scm

                script {
                    sh """
                        mvn org.owasp:dependency-check-maven:check -Dformats='ALL' -DfailOnError=false
                    """
//                    zip archive: true, glob: 'target/dependency-check-report*.*', zipFile: "dependency-check-reports.zip";
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