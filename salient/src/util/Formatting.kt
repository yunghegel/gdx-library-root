package util

import com.google.common.base.Charsets

object Format {

    const val ANSI_RESET = "\u001B[0m"
    const val ANSI_BLACK = "\u001B[30m"
    const val ANSI_RED = "\u001B[31m"
    const val ANSI_GREEN = "\u001B[32m"
    const val ANSI_YELLOW = "\u001B[33m"
    const val ANSI_BLUE = "\u001B[34m"
    const val ANSI_PURPLE = "\u001B[35m"
    const val ANSI_CYAN = "\u001B[36m"
    const val ANSI_WHITE = "\u001B[37m"

    fun ansiToUnicode(str:String) : String {
        val arr = str.byteInputStream(Charsets.UTF_8).readBytes()
        return String(arr, Charsets.UTF_8)
    }

    fun unicodeToAnsi(str:String) : String {
        val arr = str.toByteArray(Charsets.UTF_8)
        return String(arr, Charsets.UTF_8)
    }

    fun wrpAnsi(str:String, color:String) : String {
        return "${color}$str${ANSI_RESET}"
    }

    fun mbToBytes(mb:Long) : Long {
        return mb * 1024 * 1024
    }

    fun bytesToMb(bytes:Long) : Long {
        return bytes / 1024 / 1024
    }

    fun bytesToKb(bytes:Long) : Long {
        return bytes / 1024
    }


}