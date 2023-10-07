package app

import sys.profiling.MemoryProfile
import sys.profiling.OpenGLProfile
import sys.profiling.PerformanceProfile

object App {

    val memoryProfile: MemoryProfile = MemoryProfile
    val glProfile: OpenGLProfile = OpenGLProfile

    object Profiler {

        fun delegateToProxy(clazz: Class<*>,obj:Any): Any {
            return obj
        }

    }







}