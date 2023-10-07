package ui.console

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Array
import org.yunghegel.gdx.ui.UI
import org.yunghegel.gdx.ui.widgets.SLabel
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.STextButton
import org.yunghegel.gdx.ui.widgets.STextField
import sys.Log
import sys.OutputStream
import java.io.PrintStream

class Console : STable() {

    val textField = STextField(">","console")
    val directoryBtn = STextButton("/")
    val submitBtn = STextButton("Submit")


    val logEntries = STable()
    val list :List<SLabel> = List(UI.getSkin())
    val scrollPane = ScrollPane(logEntries)
    val labels = Array<SLabel>()
    val logger = Log.addLogger(this.javaClass)
    val consoleOutput = ConsoleOutput(this)

    init {

        configure()
//        System.setOut(object : PrintStream(consoleOutput) {
//            override fun println(x: String) {
//                super.println(x)
//                consoleOutput.println(x)
//            }
//
//            override fun print(s: String) {
//                super.print(s)
//                if (OutputStream.listener != null) {
//                    OutputStream.listener.onLog(s)
//                }
//            }
//        })


        System.out.print("Console initialized")
        setBackground("list-background-dark")
    }

    fun configure() {
        add(scrollPane).grow().colspan(3).row()
        add(directoryBtn).pad(2f);
        add(textField).growX().pad(2f);
        add(submitBtn).pad(2f).row()
        logEntries.add(list).grow().row()

    }

    fun addLogEntry(entry: String){
        val label = SLabel(entry,"default-medium")
        val table = STable()
        table.add(label).growX().padBottom(3f).row()

        logEntries.add(table).growX().row()
        val index = logEntries.children.size - 1
        if(index%2==0){
            table.background = UI.getSkin().getDrawable("button-over")
        } else {

        }
    }

    fun println(entry: String){
        addLogEntry(entry)

    }

}