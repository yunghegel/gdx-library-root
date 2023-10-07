package sys.profiling

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.BufferUtils
import java.nio.IntBuffer

object MemoryProfile  {

    val intBuffer: IntBuffer = BufferUtils.newIntBuffer(16)

    const val GL_NVX_GPU_MEMORY_INFO_EXT = "GL_NVX_gpu_memory_info";
    const val GL_GPU_MEM_INFO_TOTAL_AVAILABLE_MEM_NVX = 0x9048;
    const val GL_GPU_MEM_INFO_CURRENT_AVAILABLE_MEM_NVX = 0x9049;

    var JVM_TOTAL_MEMORY = Runtime.getRuntime().totalMemory()
    var JVM_FREE_MEMORY = Runtime.getRuntime().freeMemory()
    var JVM_USED_MEMORY = Runtime.getRuntime().maxMemory()

    fun getGLMaxMemory(): Int {
        intBuffer.clear()
        Gdx.gl.glGetIntegerv(GL_GPU_MEM_INFO_TOTAL_AVAILABLE_MEM_NVX, intBuffer)
        return intBuffer.get()
    }

    fun getGLFreeMemory(): Int {
        intBuffer.clear()
        Gdx.gl.glGetIntegerv(GL_GPU_MEM_INFO_CURRENT_AVAILABLE_MEM_NVX, intBuffer)
        return intBuffer.get()
    }

    fun getGLUsedMemory(): Int {
        return getGLMaxMemory() - getGLFreeMemory()
    }

    fun getFormattedMemoryString(from: MemoryFormatter.Unit, to: MemoryFormatter.Unit, num : Number): String {
        val formatted = MemoryFormatter.format(from, to, num)
        return "${formatted} ${to.name}"
    }

    override fun toString(): String {
        return "MemProfiler(JVM_TOTAL_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, JVM_TOTAL_MEMORY)}, JVM_FREE_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, JVM_FREE_MEMORY)}, JVM_USED_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, JVM_USED_MEMORY)}\n, GL_MAX_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, getGLMaxMemory())}, GL_FREE_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, getGLFreeMemory())}, GL_USED_MEMORY=${getFormattedMemoryString(MemoryFormatter.Unit.BYTE, MemoryFormatter.Unit.MEGABYTE, getGLUsedMemory())})"
    }

    object MemoryFormatter {

        enum class Unit {
            BYTE,
            KILOBYTE,
            MEGABYTE,
            GIGABYTE
        }

        fun format(inputUnit: Unit, outputUnit: Unit, value: Number) : Number {

            val conversionFactor = when (inputUnit) {
                Unit.BYTE -> when (outputUnit) {
                    Unit.BYTE -> 1
                    Unit.KILOBYTE -> 1 / 1024
                    Unit.MEGABYTE -> 1 / 1024 / 1024
                    Unit.GIGABYTE -> 1 / 1024 / 1024 / 1024
                }
                Unit.KILOBYTE -> when (outputUnit) {
                    Unit.BYTE -> 1024
                    Unit.KILOBYTE -> 1
                    Unit.MEGABYTE -> 1 / 1024
                    Unit.GIGABYTE -> 1 / 1024 / 1024
                }
                Unit.MEGABYTE -> when (outputUnit) {
                    Unit.BYTE -> 1024 * 1024
                    Unit.KILOBYTE -> 1024
                    Unit.MEGABYTE -> 1
                    Unit.GIGABYTE -> 1 / 1024
                }
                Unit.GIGABYTE -> when (outputUnit) {
                    Unit.BYTE -> 1024 * 1024 * 1024
                    Unit.KILOBYTE -> 1024 * 1024
                    Unit.MEGABYTE -> 1024
                    Unit.GIGABYTE -> 1
                }
            }

            return value.toDouble() * conversionFactor.toDouble()
        }

    }

}