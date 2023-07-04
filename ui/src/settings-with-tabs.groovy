tabs {
	tab1 {
		name = 'Tab #1'
		groups{
			group1 {
				name = 'Group #1'
				task1 {
					name='Task #1'
					commandLine = 'cmd /C ECHO Hello World héhé 11 %1'
					outputCharset = 'cp850'
					promptArg = "Enter arg please:"
				}
				task2 {
					name='Task #2'
					commandLine = 'cmd /C ECHO Hello World 12'
				}
				task3 {
					name='Task #3'
					commandLine = 'cmd /C ECHO Hello World 13'
				}
				task4 {
					name='Task #4'
					commandLine = 'cmd /C ECHO Hello World 14'
				}
				task5 {
					name='Task #5'
					commandLine = 'cmd /C ECHO Hello World 5'
				}
				task6 {
					name='Task #6'
					commandLine = 'cmd /C ECHO Hello World 6'
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
		}
	}
	tab2 {
		name = 'Tab #2'
		groups{
			group1 {
				name = 'Group #2.1'
				task1 {
					name='Task #2.1'
					commandLine = 'cmd /C ECHO Hello World héhé 11 %1 from tab #2'
					outputCharset = 'cp850'
					promptArg = "Enter arg please:"
				}
				task2 {
					name='Task #2.2'
					commandLine = 'cmd /C ECHO Hello World 12 from tab #2'
				}
			}
			group2 {
				name = 'Group #2.2'
				task1 {
					name='Task #2.1'
					commandLine = 'cmd /C ECHO Hello World 21 from tab #2'
				}
				task2 {
					name='Task #2.2'
					commandLine = 'cmd /C ECHO Hello World 22 from tab #2'
				}
				task3 {
					name='Task #2.3 (long text)'
					commandLine = 'cmd /C ECHO Hello Woooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooorld 22 from tab #2'
				}
				//		task3 {
				//			name='Task #3 (BAD command)'
				//			commandLine = 'cmd1'
				//		}
			}
			group3 {
				name = 'Group #2.3 - Repeatable tasks'
				task1 {
					name='Task #2.1 - Repeatable'
					commandLine = 'cmd /C ECHO Hello World from tab #2 @ %date% %time%'
					repeatCount = 5
				}
			}
		}
	}
}