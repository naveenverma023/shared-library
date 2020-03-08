def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {
        agent any
	stages{
        stage('checkout'){
            steps{
                git branch: pipelineParams.branch, credentialsId: pipelineParams.credentials, url: pipelineParams.scmUrl
            }

        }
    }
    }
}
