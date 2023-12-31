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
        stage('Vulnerabilities check') {
            parallel {
                stage('Dependency check') {
                    options {
                        timeout(time: 30, unit: 'MINUTES')
                    }
                    steps {
                        checkout scm

                        script {
                            sh """
                        mvn org.owasp:dependency-check-maven:check -Dformats='ALL' -DfailOnError=false
                        rm ./dependency-check-reports.zip
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
                            CURRENT_VERSION = readMavenPom().getVersion()
                            sh """
                        mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom -DincludeTestScope=true -DprojectType=library
                        """
//                            withCredentials([string(credentialsId: 'dependency-track-token', variable: 'API_KEY')]) {
//                                dependencyTrackPublisher artifact: './target/bom.json', dependencyTrackApiKey: API_KEY, projectName: 'core-VNE', projectVersion: '5.3', synchronous: true, projectProperties: [tags: ['microservice', 'core', 'ets'], group: 'ETS-group', parentId: 'e1451e9c-b4d5-4670-b6af-42bcedbf3d79']
//                            }
                            withCredentials([string(credentialsId: 'new-dependency-track-token', variable: 'API_KEY')]) {
                                dependencyTrackPublisher artifact: './target/bom.json', dependencyTrackApiKey: API_KEY, projectName: 'core-Vit', projectVersion: '5.3', synchronous: true, projectProperties: [tags: ['microservice', 'dev', 'ets'], group: 'dev', parentId: '607d4039-c03f-4c82-892e-5b2f26103370']
                            }
                        }//script
                    }//steps
                }//stage
            }
        }
        stage('Version') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                script {
                    CURRENT_VERSION = sh(returnStdout: true, script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout").trim()
                    echo "$CURRENT_VERSION"
                    CURRENT_VERSION_MAVEN_PLUGIN = readMavenPom().getVersion()
                    echo "$CURRENT_VERSION_MAVEN_PLUGIN"
                }//script
            }//steps
        }//stage
    }
}