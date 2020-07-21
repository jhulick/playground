package nc.devops.shared.library.xray.runner

class JUnitTestRunnerStrategy implements TestRunnerStrategy {
    private final XrayClient xrayClient
    private final String testsResultsPath

    JUnitTestRunnerStrategy(XrayClient xrayClient, String testsResultsPath) {
        this.xrayClient = xrayClient
        this.testsResultsPath = testsResultsPath
    }

    @Override
    def importExecutionResults(Map vars) {
        def script = vars.script
        def currentDir = new File("$vars.workspace/$testsResultsPath")
        script.echo "Xray: importing junit execution results from $currentDir..."
        def responses = []
        currentDir.listFiles().each { file ->
            if (file.name.endsWith(".xml")) {
                def response = xrayClient.postExecutionResults(vars, file.text)
                script.echo 'XRay: junit execution results import success'
                responses << response
            }
        }
        script.echo "Xray: processed ${responses.size()} junit xml files"
        return responses
    }
}
