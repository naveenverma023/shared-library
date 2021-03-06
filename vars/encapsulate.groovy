def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

 pipeline{
    agent any
    
    tools{
        maven 'maven-default'
    }

    stages{
        stage('Clean workspace'){
            steps{
                deleteDir()
            }
        }
        
        stage('Invoking shared library'){
            steps{
                hello()
                hello 'Naveen'
            }
        }
        
        stage('Git checkout'){
            steps{
                git branch: pipelineParams.branch, credentialsId: pipelineParams.credentials, url: pipelineParams.url
            }
            
        }
        
        stage('Build'){
            steps{
                sh 'mvn clean ${goal}'
            }
        }
        
        stage('Run Unit test'){
            steps{
                sh 'mvn test site'
            }
        }
        
        stage('Run checkstyle'){
            steps{
                sh 'mvn checkstyle:checkstyle'
            }
        }
        
        stage('Run PMD'){
            steps{
                sh 'mvn pmd:pmd'
            }
        }
        
        stage('cobertura'){
            steps{
                sh 'mvn cobertura:cobertura'
            }
        }
        
        stage('Publish HTML'){
            steps{
                publishhtml(directory: pipelineParams.directory, files: pipelineParams.files, title: pipelineParams.title)
            }
        }
    }
}


}
