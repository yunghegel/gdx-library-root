package ui.console

import java.io.PrintStream

class CommandLinePrinter(val commandLineOutput: CommandLineOutput) : PrintStream(System.out) {

    override fun println(text: String?) {
        commandLineOutput.println(text)
    }

}