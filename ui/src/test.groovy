import javax.swing.JFrame
import javax.swing.JScrollPane

import groovy.swing.SwingBuilder

def swingBuilder = new SwingBuilder()
swingBuilder.edt {
	frame(title: 'Scroll Pane Example', defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
		scrollPane(verticalScrollBarPolicy: JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) {
			textArea(rows: 10, columns: 20, text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" * 20)
		}
		size(300, 200)
		show()
	}
}