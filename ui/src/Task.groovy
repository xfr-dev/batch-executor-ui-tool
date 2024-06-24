



public class Task {

	def name
	def commandLine
	def outputCharset
	def promptArg
	def passwordPromptArg

	def promptArgValue
	def passwordPromptArgValue

	def repeatCount
	def repeatInterval

	def url
	def display

	def selected = false

	String toString() {
		return name
	}

	public static enum Display {
		CHECKBOX, LINK, BUTTON, CHECKBOX_LINK, CHECKBOX_BUTTON
	}
}
