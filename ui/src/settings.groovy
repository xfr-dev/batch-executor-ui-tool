
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
}

