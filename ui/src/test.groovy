import groovy.swing.SwingBuilder
import javax.swing.WindowConstants

tabs = ['Tab #1','Tab #2']

new SwingBuilder().edt {

		frame(title:'Test window', size: [800, 600], show:true, defaultCloseOperation:WindowConstants.EXIT_ON_CLOSE, locationRelativeTo: null) {

			lookAndFeel('system')

			vbox { 
			tabbedPane(id: 'tabs') {
			    for(tab in tabs) { 
					panel(name: tab) { 
					}
				}
			}
			}
			}
			}