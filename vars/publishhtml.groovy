def call(Map publishParams){
publishHTML([
allowMissing: false, 
alwaysLinkToLastBuild: false, 
includes: '**/*.html', 
keepAll: false, 
reportDir: publishParams.directory, 
reportFiles: publishParams.files, 
reportName: publishParams.title, 
])
}
