def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

 pipeline{
    agent any
    
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
                // git branch: pipelineParams.branch, credentialsId: pipelineParams.credentials, url: pipelineParams.url
				gitcheckout(branch: pipelineParams.branch, url: pipelineParams.url, credentials: pipelineParams.credentials)
            }
            
        }
        
    }
}

}
