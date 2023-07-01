



class Task {

	def name
	def commandLine
	def outputCharset
	def promptArg

	def repeatCount
	def repeatInterval

	def selected = false

	String toString() {
		return name
	}
}
