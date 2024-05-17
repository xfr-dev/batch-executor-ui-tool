# batch-executor-ui-tool
Batch executor ui tool to easily launch one or multilples system commands.

Made in Groovy and only tested on Windows.

# Screenshots
![image](https://github.com/xfr-dev/batch-executor-ui-tool/assets/9008263/175d203a-6c95-4573-846d-2ea20f6841dd)

# Features
- Tab of groups of commands
- Group of commands
- Buttons to select all/none for a or all groups
- Checkbox / button / link for a single command
- Repeatable task/command
- User inputs/variables (prompt) - including visible string and password

# Usage

## Prerequesite
Download and install a Groovy distro (of version > 2.3.3) at https://groovy.apache.org/download.html

## Config
Create a configuration file.
See [this example](ui/src/settings.groovy)
See [this example with tabs](ui/src/settings-with-tabs.groovy)

## Run
Use this command to run the tool : `groovy run <your-config-filepath>`

