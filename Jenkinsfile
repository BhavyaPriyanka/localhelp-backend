@Library('jenkins-shared-library@main') _

def configMap = [

    type: "javaEKS",
    appDir: "backend",
    sonarProjectKey: "localhelp-backend",
    component: "localhelp-backend",
    project: "localhelp"
]

pipelineDecision.decidePipeline(configMap)