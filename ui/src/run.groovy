import java.nio.file.FileSystems

def configPath = 'settings.groovy'
if (args.length > 0 && args[0]) {
	configPath = args[0]
}

println "Groovy version is : ${GroovySystem.getVersion()}"
println "Groovy home is : ${System.getProperty('groovy.home')}"
println "Default locale is ${Locale.getDefault()}"
println "Current directory is : ${FileSystems.getDefault().getPath("").toAbsolutePath()}"

println "Launching batch task executor main window using configuration file : [${configPath}]"
def config = new ConfigSlurper().parse(new File(configPath).toURL())
URL bundle = TaskExecutorMainWindow.class.getResource("/applicationMessages.properties")
println "Bundle is : [${bundle}]"
TaskExecutorMainWindow window = new TaskExecutorMainWindow(config)
window.show()