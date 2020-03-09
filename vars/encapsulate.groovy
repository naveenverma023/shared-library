def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    @Library('shared-library')_

pipeline{
    agent any
    
    parameters {
        string(name: 'branch', defaultValue: '', description: 'SCM branch from which you want to checkout your code')
        string(name: 'url', defaultValue: '', description: 'SCM repository url from which you want to checkout your code')
        password(defaultValue: '', description: 'Credentials for your SCM', name: 'credentials')
        string(name: 'directory', defaultValue: '', description: 'Directory for all the html files')
        string(name: 'files', defaultValue: '', description: 'The html reports to publish')
        string(name: 'title', defaultValue: '', description: 'Title for your HTML reports')
    }
    
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
                gitcheckout(branch: "${branch}", url: "${url}", credentials: "${credentials}")
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
                publishhtml(directory: "${directory}", files: "${files}", title: "${title}")
            }
        }
    }
}


}
