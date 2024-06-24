import Task.Display

bundle = new File("../test/applicationMessages.properties")
task.defaultDisplay = Display.CHECKBOX_BUTTON

groups{
	group1 {
		name = 'Group #1'
		task1 {
			name='Task #1 (with arg)'
			commandLine = 'cmd /C ECHO Hello World héhé 11 %1'
			outputCharset = 'cp850'
			promptArg = "Enter arg please:"
		}
		task2 {
			name='Task #2 (with password)'
			commandLine = 'cmd /C ECHO Hello World 12 %1'
			passwordPromptArg = "Enter password please:"
		}
	}
	group2 {
		name = 'Group #2'
		task1 {
			name='Task #1'
			commandLine = 'cmd /C ECHO Hello World 21'
		}
		task2 {
			name='Task #2'
			commandLine = 'cmd /C ECHO Hello World 22'
		}
		task3 {
			name='Task #3 (long text)'
			commandLine = 'cmd /C ECHO Hello Woooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooorld 22'
		}
		//		task3 {
		//			name='Task #3 (BAD command)'
		//			commandLine = 'cmd1'
		//		}
	}
	group3 {
		name = 'Group #3 - Repeatable tasks'
		task1 {
			name='Task #1 - Repeatable'
			commandLine = 'cmd /C ECHO Hello World @ %date% %time%'
			repeatCount = 5
			repeatInterval = 1000
		}
		task2 {
			name='Task #2 - Repeatable with prompt'
			commandLine = 'cmd /C ECHO Hello World @ %date% %time% %1'
			repeatCount = 5
			promptArg = "Enter arg please:"
			repeatInterval = 1000
		}
		task3 {
			name='Task #3 - Repeatable with password prompt'
			commandLine = 'cmd /C ECHO Hello World @ %date% %time% %1'
			repeatCount = 5
			passwordPromptArg = "Enter password please:"
			repeatInterval = 1000
		}
	}
	group4 {
		name = 'Group #4 - Urls'
		url1 {
			name='Url (checkbox)'
			url = 'https://www.google.be'
		}
		url2 {
			name='Url (link)'
			url = 'https://www.atlassian.com'
			display = 'LINK'
		}
		url3 {
			name='Url (Button)'
			url = 'https://thecodinglove.com/'
			display = 'BUTTON'
		}
	}

	group5 {
		name = 'Group #5 - Urls with checkboxes'
		url1 {
			name='Url (link)'
			url = 'https://www.atlassian.com'
			display = 'CHECKBOX_LINK'
		}
		url2 {
			name='Url (Button)'
			url = 'https://thecodinglove.com/'
			display = 'CHECKBOX_BUTTON'
		}
	}
}

