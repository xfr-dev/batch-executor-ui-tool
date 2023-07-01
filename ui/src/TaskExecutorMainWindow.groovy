import groovy.swing.SwingBuilder

import java.awt.Color
import java.nio.charset.Charset

import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import javax.swing.border.TitledBorder





class TaskExecutorMainWindow {

	def static final SIMULATION_MODE = false

	def config

	def groups = new ArrayList<Group>()

	def bundle = java.util.ResourceBundle.getBundle("applicationMessages", Locale.getDefault(), TaskExecutorMainWindow.class.getClassLoader())

	// UI properties
	def checkboxes = new ArrayList<>();

	def components = new ArrayList<>();

	def TaskExecutorMainWindow(config) {

		this.config = config

		fillGroups(config.groups)

		for(group in groups) {
			println "$group"
			for(task in group.tasks) {
				println "$task.name $task.commandLine"
			}
		}		
	}

	def fillGroups( map, group = null, task = null, level = 0 ) {
		map.each { key, value ->
			if( value instanceof Map ) {
				println "$key"

				if (level == 0) {

					group = new Group(name: value.get("name"))
					groups.add(group)
					fillGroups( value, group, task, level + 1 )
				} else if (level == 1) {

					task = new Task(name: value.get("name"), commandLine: value.get("commandLine"), outputCharset: value.get("outputCharset") ? value.get("outputCharset") : Charset.defaultCharset().name(), promptArg: value.get("promptArg"), repeatCount:  value.get("repeatCount") ? value.get("repeatCount") : 1, repeatInterval: value.get("repeatInterval") ? value.get("repeatInterval") : 5000 )
					group.tasks.add(task)
				}
			}
			else {
				println "$group $task $key : $value"
			}
		}
	}

	def show() {
		new SwingBuilder().edt {

			frame(title:txt('application.title'), size: [800, 600], show:true, defaultCloseOperation:WindowConstants.EXIT_ON_CLOSE, locationRelativeTo: null) {

				lookAndFeel('system')

				// Menu
				menuBar() {
					menu(text: 'File', mnemonic: 'F') {
						menuItem(text: 'Exit', mnemonic: 'X', actionPerformed: {dispose() })
					}
				}

				// Global layout
				gridLayout(columns: 1, rows: 2 + groups.size())

				// Global selection buttons panel
				panel() {
					boxLayout();
					components.add(button(text:txt('button.selectAll'),actionPerformed: {
						checkboxes.each {checkbox ->
							checkbox.selected = true
						}
					}))
					components.add(button(text:txt('button.selectNone'),actionPerformed: {
						checkboxes.each {checkbox ->
							checkbox.selected = false
						}
					}))
				}

				// Tasks groups panels
				groups.each { group ->
					def border = titledBorder(border: lineBorder(color: Color.black), title: group.name, titleJustification: TitledBorder.LEFT, titlePosition: TitledBorder.LEFT);
					panel(border: border, preferredSize: [400, 600]) {
						boxLayout(axis:BoxLayout.Y_AXIS)
						//gridLayout(columns: 1, rows: 2, vgap: 1, hgap: 1 )
						panel() {
							components.add(button(text:txt('button.selectAll'),actionPerformed: {
								def panel = findGroupPanel(it.source)
								panel.components.each { component ->
									if (component instanceof JCheckBox) {
										component.selected = true
									}
								}
							}))
							components.add(button(text:txt('button.selectNone'),actionPerformed: {
								def panel = findGroupPanel(it.source)
								panel.components.each { component ->
									if (component instanceof JCheckBox) {
										component.selected = false
									}
								}
							}))
						}
						panel(name: 'tasksGroupPanel') {
							group.tasks.each { task ->
								checkboxes.add( checkBox(text: task.name, selected: bind(target:task, targetProperty: 'selected')) )
							}
						}
					}
				}

				// Execute button panel
				panel() {
					boxLayout();
					components.add(button(text:txt('button.execute'),
					actionPerformed: { doOutside { executeSelectedTasks() } }))
				}

				components.addAll(checkboxes)
			}
		}
	}

	private executeSelectedTasks() {

		def selectedTasks = new ArrayList<>();

		groups.each {group->
			group.tasks.each {task->
				if (task.selected) {
					selectedTasks.add(task)
				}
			}
		}

		println "Executing selected tasks : [${selectedTasks}]"
		freeze()
		TasksExecutionWindow executionWindow = new TasksExecutionWindow(this, selectedTasks)
		try {
			executionWindow.show()
			executionWindow.executeTasks()
		} catch(Throwable e) {
			e.printStackTrace()
			JOptionPane.showMessageDialog(executionWindow.frame, "Unexpected error occured while executing selected task : ${e.message}",
					txt("tasks.execution.error.title"), JOptionPane.ERROR_MESSAGE);
		} finally {
			unFreeze()
		}
	}

	def findGroupPanel(def fromButtonPanel) {
		def panel = SwingUtilities.getAncestorOfClass(JPanel.class, fromButtonPanel)
		panel = SwingUtilities.getAncestorOfClass(JPanel.class, panel)
		for(component in panel.components) {
			if (component instanceof JPanel && component.name.equals('tasksGroupPanel')) {
				return component;
			}
		}
	}

	def txt(String key) {
		return bundle.getString(key);
	}

	def freeze() {
		components.each {component->
			component.enabled = false
		}
	}

	def unFreeze() {
		components.each {component->
			component.enabled = true
		}
	}
}
