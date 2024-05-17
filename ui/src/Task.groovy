



public class Task {

	def name
	def commandLine
	def outputCharset
	def promptArg
	def passwordPromptArg

	def repeatCount
	def repeatInterval

	def url
	def display = Display.CHECKBOX

	def selected = false

	String toString() {
		return name
	}

	public static enum Display {
		CHECKBOX, LINK, BUTTON, CHECKBOX_LINK, CHECKBOX_BUTTON
	}
}
