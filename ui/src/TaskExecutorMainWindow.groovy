import static Task.Display

import java.awt.Color
import java.awt.Cursor
import java.nio.charset.Charset

import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import javax.swing.border.TitledBorder
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener

import groovy.swing.SwingBuilder




class TaskExecutorMainWindow {

	def static final SIMULATION_MODE = false

	def config

	def tabs = new ArrayList<Tab>()

	def tabbedPane

	def selectedTab

	def bundle = java.util.ResourceBundle.getBundle("applicationMessages", Locale.getDefault(), TaskExecutorMainWindow.class.getClassLoader())

	// UI properties

	def allComponents = new ArrayList<>()

	def TaskExecutorMainWindow(config) {

		this.config = config

		if (!config.tabs) {

			def tab = new Tab()
			tabs.add(tab)
			fillGroups(config.groups, null, null, 0, tab)
			print(tab)
		} else {

			for(tabConfigEntry in config.tabs) {

				def tabConfigValue = tabConfigEntry.value
				def tab = new Tab(name: tabConfigValue.name)
				tabs.add(tab)
				println "Created tab : $tab"

				fillGroups(tabConfigValue.groups, null, null, 0, tab)
			}
		}
	}

	def printTab(tab) {

		for(group in tab.groups) {

			println "$group"

			for(task in group.tasks) {

				println "$task.name $task.commandLine $task.url"
			}
		}
	}

	def fillGroups( map, group = null, task = null, level = 0, tab = null ) {

		map.each { key, value ->

			if( value instanceof Map ) {

				println "$key"
				println "$level"

				if (level == 0) {

					group = new Group(name: value.get("name"))
					tab.groups.add(group)
					fillGroups( value, group, task, level + 1, tab )
				} else if (level == 1) {

					task = new Task(name: value.get("name"), commandLine: value.get("commandLine"),
					outputCharset: value.get("outputCharset") ? value.get("outputCharset") : Charset.defaultCharset().name(),
					promptArg: value.get("promptArg"),
					repeatCount:  value.get("repeatCount") ? value.get("repeatCount") : 1,
					repeatInterval: value.get("repeatInterval") ? value.get("repeatInterval") : 5000,
					url: value.get("url"),
					display: value.get("display") ? Display.valueOf(value.get("display")) : Display.CHECKBOX)

					group.tasks.add(task)
				}
			} else {

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

				boxLayout(axis: BoxLayout.PAGE_AXIS)
				// Tabs
				tabbedPane = tabbedPane(id: 'tabs') {
					//				tabbedPane(id: 'tabs', tabLayoutPolicy:JTabbedPane.SCROLL_TAB_LAYOUT) {

					for(tab in tabs) {

						println "Adding tab [$tab] to tab in ui"
						checkboxes = tab.checkboxes
						panel( name: tab.name ) {

							groups = tab.groups
							// Global layout
							gridLayout(columns: 1, rows: 2 + groups.size())

							// Global selection buttons panel
							panel() {
								boxLayout()
								allComponents.add(button(text:txt('button.selectAll'),actionPerformed: {
									selectedTab.checkboxes.each { checkbox ->
										checkbox.selected = true
									}
								}))
								allComponents.add(button(text:txt('button.selectNone'),actionPerformed: {
									selectedTab.checkboxes.each { checkbox ->
										checkbox.selected = false
									}
								}))
							}

							// Tasks groups panels
							groups.each { group ->
								def border = titledBorder(border: lineBorder(color: Color.black), title: group.name, titleJustification: TitledBorder.LEFT, titlePosition: TitledBorder.LEFT)
								panel(border: border) {
									//									panel(border: border, preferredSize: [400, 600]) {
									boxLayout(axis:BoxLayout.Y_AXIS)
									//gridLayout(columns: 1, rows: 2, vgap: 1, hgap: 1 )
									panel() {
										allComponents.add(button(text:txt('button.selectAll'),actionPerformed: {
											def panel = findGroupPanel(it.source)
											panel.components.each { component ->
												if (component instanceof JCheckBox) {
													component.selected = true
												}
											}
										}))
										allComponents.add(button(text:txt('button.selectNone'),actionPerformed: {
											def panel = findGroupPanel(it.source)
											panel.components.each { component ->
												if (component instanceof JCheckBox) {
													component.selected = false
												}
											}
										}))
									}

									//scrollPane() {

									panel(name: 'tasksGroupPanel') {

										boxLayout(axis:BoxLayout.X_AXIS)
										//gridLayout(columns:4, rows: 2, vgap: 1, hgap: 1, alignment: GridLayout.CENTER )
										group.tasks.each { task ->

											if (task.display == Display.CHECKBOX) {

												checkboxes.add( checkBox(text: task.name, selected: bind(target:task, targetProperty: 'selected')) )
											} else if (task.display == Display.LINK) {
												
												def link = label(text: task.name, foreground: Color.BLUE.darker(), cursor: Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
												mouseClicked: {
													java.awt.Desktop.getDesktop().browse(task.url.toURI())
												}
												)
												
												allComponents.add(link)

											} else if (task.display == Display.BUTTON) {

												def button = button(text: task.name, actionPerformed: { e ->
													TasksExecutionWindow execution = new TasksExecutionWindow(this,List.of(task))
													execution.show()
													execution.executeTasks()
												})

												allComponents.add(button)
											}
										}
									}
									//}
								}
							}
							allComponents.addAll(checkboxes)
						}
					}
				}
				vbox{
					// Execute button panel
					panel(name: "Actions") {
						boxLayout()
						allComponents.add(button(text:txt('button.execute'),
						actionPerformed: {
							doOutside {
								executeSelectedTasks()
							}
						}))
					}
				}
			}

			tabbedPane.addChangeListener({e ->

				selectedTabIndex = tabbedPane.selectedIndex
				println("Selected tab index: $selectedTabIndex")

				selectedTab = tabs.getAt(selectedTabIndex)
				println("Selected tab is: $selectedTab")
			})

			selectedTab = tabs.getAt(0)
		}
	}

	private executeSelectedTasks() {

		def groups = selectedTab.groups

		def selectedTasks = new ArrayList<>()

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
					txt("tasks.execution.error.title"), JOptionPane.ERROR_MESSAGE)
		} finally {
			unFreeze()
		}
	}

	def findGroupPanel(def fromButtonPanel) {
		def panel = SwingUtilities.getAncestorOfClass(JPanel.class, fromButtonPanel)
		panel = SwingUtilities.getAncestorOfClass(JPanel.class, panel)
		for(component in panel.components) {
			if (component instanceof JPanel && component.name.equals('tasksGroupPanel')) {
				return component
			}
		}
	}

	def txt(String key) {
		return bundle.getString(key)
	}

	def freeze() {
		allComponents.each {component->
			component.enabled = false
		}
	}

	def unFreeze() {
		allComponents.each {component->
			component.enabled = true
		}
	}
}
