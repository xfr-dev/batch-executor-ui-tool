

class Tab {
	def name = "Default"
	def groups = new ArrayList<Group>()
	def checkboxes = new ArrayList<>()

	String toString() {
		return name
	}
}
