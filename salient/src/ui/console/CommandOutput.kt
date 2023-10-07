package ui.console

class CommandOutput(val stdOut: CommandLinePrinter, val stdErr: CommandLinePrinter) {

    fun println(text: String) {
        stdOut.println(text)
    }

    fun printError(text: String) {
        stdErr.println(text)
    }





}