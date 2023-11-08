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
                    CURRENT_VERSION = readMavenPom().getVersion()
                    sh """
                        mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom -DincludeTestScope=true -DprojectType=library
                        ls && ls ./target
                    """
                    withCredentials([string(credentialsId: 'dependency-track-token', variable: 'API_KEY')]) {
                        dependencyTrackPublisher artifact: './target/bom.json', dependencyTrackApiKey: API_KEY, projectName: 'ci-integrated-2', projectVersion: "1.5.0", synchronous: true
                    }
                }//script
            }//steps
        }//stage

        stage('Version') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                script {
                    CURRENT_VERSION = sh (returnStdout: true, script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout").trim()
                    echo "$CURRENT_VERSION"
                    CURRENT_VERSION_MAVEN_PLUGIN = readMavenPom().getVersion()
                    echo "$CURRENT_VERSION_MAVEN_PLUGIN"
                }//script
            }//steps
        }//stage
    }
}