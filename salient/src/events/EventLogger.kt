package events

import com.badlogic.gdx.files.FileHandle
import com.google.common.base.Charsets
import com.google.common.base.Utf8
import events.lifecycle.ShutdownEvent
import io.FileService
import org.yunghegel.gdx.utils.StringUtils
import org.yunghegel.gdx.utils.console.ConsoleUtils
import sys.Log
import util.Format
import java.beans.Encoder
import java.io.ByteArrayOutputStream
import java.io.Writer
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.function.Consumer
import java.util.logging.LogRecord
import java.util.stream.Collectors
import java.util.stream.Stream


open class EventLogger  {










    init {

    }




    enum class State {
        CREATED, POSTED, HANDLED
    }


    data class  EventLog(val event: Event, val state: State) {

         var postTime: Instant = Instant.now()
         var handleTime: Instant = Instant.now()
        lateinit var desc: String

        init {
            desc = when(state) {
                State.CREATED -> created()
                State.POSTED -> posted()
                State.HANDLED -> handled()
            }
        }

        override fun toString(): String {


            return desc
        }

        fun  time() : String {
            return (SimpleDateFormat("HH:mm:ss").format(Date()).toString())
        }

        fun head():String {
            return "[EVENT: ${event::class.java.simpleName} ${state.name}] ${time()} \n"
        }

        fun stack(strBuilder:StringBuilder?) :StringBuilder {

            val builder = StringBuilder().append("\n")
            val startIdx = 1

            StackWalker.getInstance().walk { s: Stream<StackWalker.StackFrame?> ->
                s.limit(
                    10
                )
                    .collect(Collectors.toList<Any?>()).forEach(Consumer { obj: Any? ->
                        builder.appendLine(obj.toString())
                    })
            }
            val endIdx  = builder.length - 1

            builder.insert(startIdx ,("STACK TRACE: \n"))
            builder.insert(endIdx, "\nEND STACK TRACE \n")

            return if(strBuilder != null) {
                strBuilder.appendLine(builder.toString())
                strBuilder
            } else {
                builder
            }

        }

        fun created(): String {
            val record = StringBuilder().append("\n")


            record.append(head()).append("\n")

            stack(record).append("\n")




            return record.toString()
        }

        fun ansiToUnicode(str:String) : String {
//            validate that the string is ansi


            val arr = str.byteInputStream(Charsets.UTF_8).readBytes()
            var utf= String(arr, Charsets.UTF_8)
            while (utf.contains(Format.ANSI_RESET)) {
                utf = utf.replace(Format.ANSI_RESET, "")
            }
            return utf

        }


        fun posted():String {
            postTime = Instant.now()

            val record = ConsoleUtils.getCallingMethodName()
            return "[EVENT: ${event::class.java.simpleName}  POSTED] ${time()} \n SOURCE: $record \n"
        }

        fun handled():String {
            handleTime = Instant.now()
            return "[EVENT: ${event::class.java.simpleName} HANDLED] ${time()} \n"
        }

    }

    companion object {


    }




}

open class DefaultLogger:EventLogger(), Event.Listener, ShutdownEvent.Listener {

    val stack = Stack<EventLogger.EventLog>()
    val logFile : FileHandle
    val logFileWriter : Writer
    val bytesOut = ByteArrayOutputStream()

    init {

        logFile = FileService.getLogFile()
        logFileWriter = logFile.writer(true)

    }

    fun checkLineCount() {
        val lines = logFile.reader().readLines()
        if(lines.size > 2000) {
            val newLines = lines.subList(1000, lines.size)
            logFileWriter.write(newLines.joinToString("\n"))
            logFileWriter.flush()
        }
    }

    fun log(event: Event, state: EventLogger.State) {
        checkLineCount()
        val log = EventLogger.EventLog(event , state)
        println(log)
        stack.push(log)

        logFileWriter.write((log.toString()))
        logFileWriter.flush()



    }

    override fun onShutdown(event: ShutdownEvent) {
        logFileWriter.write("Event Log: \n")


        logFileWriter.close()
        logFileWriter.flush()
    }

    override fun eventPosted(event:Event) {
         log(event, EventLogger.State.POSTED)
    }

    override fun eventHandled(event: Event): Boolean {
    log(event, EventLogger.State.HANDLED)
        return true
    }

    override fun eventCreated(event: Event) {
        log(event, EventLogger.State.CREATED)
    }


}