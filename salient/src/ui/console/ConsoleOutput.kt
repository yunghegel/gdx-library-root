package ui.console

import java.io.PrintStream

class ConsoleOutput(val console: Console) : PrintStream(System.out,true), CommandLineOutput {



        override fun print(text: String?) {
            if (text != null) {
                console.println(text)
            }
        }

        override fun println(text: String?) {
            if (text != null) {
                console.println(text)
            }
        }

}