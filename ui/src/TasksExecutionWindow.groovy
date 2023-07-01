import groovy.swing.SwingBuilder

import javax.swing.BoxLayout
import javax.swing.WindowConstants
import javax.swing.text.DefaultCaret



class TasksExecutionWindow {

	static final String lf =  System.getProperty("line.separator")

	TaskExecutorMainWindow parent
	def tasks

	// UI properties
	def frame
	def outputTextArea
	def progressBar


	def TasksExecutionWindow(TaskExecutorMainWindow parent, def tasks) {
		this.parent = parent
		this.tasks = tasks
	}

	def show() {
		new SwingBuilder().edt {

			frame = frame(title:parent.txt('tasks.execution.title'), size: [1024, 600], show:true, defaultCloseOperation:WindowConstants.DISPOSE_ON_CLOSE, locationRelativeTo: null) {

				lookAndFeel('system')
				boxLayout(axis:BoxLayout.Y_AXIS)
				scrollPane() {
					outputTextArea = textArea(rows: 20, columns: 120, editable: false)
					DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
					caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				}
				progressBar = progressBar(minimum:0, maximum: tasks.size(), value: 0, stringPainted: true)
			}
		}
	}

	def executeTasks() {
		
		tasks.each {task->
		
		 	println "Execution task [${task}] ${task.repeatCount} time(s)."
		 	if (task.repeatCount > 1) {
		 		progressBar.maximum = task.repeatCount
		 	}
			for(def i = 0 ; i < task.repeatCount ; i++) {
			
				executeTask(task)
				
				if ( task.repeatCount > 1 ) {
				 
					Thread.sleep(task.repeatInterval)
				}
			}
		} 
		
		frame.title = parent.txt('tasks.execution.finished.title')
	}

	def executeTask(task) {

		taskStarted(task)

		def arg

		if (task.promptArg) {
			def readln = javax.swing.JOptionPane.&showInputDialog
			arg = readln task.promptArg
			//println "Arg is : [${arg}]"
		}

		if (parent.SIMULATION_MODE) {
			println "Program set in simulation mode, faking execution of task [${task}] with arg [${arg}]"
			Thread.sleep(1500)
			appendOutput(lf)
			appendOutput("Program set in simulation mode, faked task ${task} execution")
			appendOutput(lf)
		} else {
			def commandLine = arg ? task.commandLine.replaceAll("%1", arg): task.commandLine
			//println "Executing command line : [${commandLine}]"
			def process = commandLine.execute()
			def output = new OutputStream() {

						void write(int arg0) throws IOException {
							throw new UnsupportedOperationException("This method cannot be invoked")
						}
						void write(byte[] character, int offset, int length) throws IOException {
							appendOutput(new String(Arrays.copyOfRange(character, offset, offset + length), task.outputCharset))
						}
					}
			//			def output = new OutputStream() {
			//
			//										def pos = 0
			//										byte[] buffer = new byte[100]
			//
			//										void write(int character) throws IOException {
			//											//println "Writing character : [${character}]"
			//											buffer[pos++] = character
			//											if (pos == buffer.length) {
			//												flush()
			//											}
			//										}
			//
			//										void flush() throws IOException {
			//											appendOutput(new String(Arrays.copyOfRange(buffer, 0, pos), task.outputCharset))
			//											pos = 0
			//										}
			//
			//										void close() throws IOException {
			//											flush()
			//										}
			//									}
			try {
				output = new BufferedOutputStream(output, 100);
				process.consumeProcessOutput(output, output)

				def resultCode = process.waitFor()
				if (resultCode != 0) {
					throw new RuntimeException("Task [${task}] execution has failed, see logs for details.")
				}
			} finally {
				output.close()
			}
		}

		taskFinished(task)
	}

	def taskStarted(task) {

		progressBar.string = "Executing task [${task}] : ${progressBar.value + 1} / ${progressBar.maximum}"
		def message = "Starting task \"${task}\""
		def separator = createSeparator(message.length())
		appendOutput(lf)
		appendOutput(separator)
		appendOutput(lf)
		appendOutput(message)
		appendOutput(lf)
		appendOutput(separator)
		appendOutput(lf)
	}
	
	def createSeparator(length) { 
		
		def result = ""
		
		for(int i = 0 ; i < length ; i++) { 
			result += '-';
		}
		
		return result
	}

	def taskFinished(task) {

		progressBar.string = "Task [${task}] finished : ${progressBar.value + 1} / ${progressBar.maximum}"
		progressBar.value +=1
		
		def message = "Task \"${task}\" finished"
		def separator = createSeparator(message.length())
		
		appendOutput(lf)
		appendOutput(separator)
		appendOutput(lf)
		appendOutput(message)
		appendOutput(lf)
		appendOutput(separator)
		appendOutput(lf)
	}

	def appendOutput(txt) {
		//println "Appending text : [${txt}]"
		outputTextArea.text += txt
		outputTextArea.setCaretPosition(outputTextArea.getLineStartOffset(outputTextArea.getLineCount() - 1))
	}
}
