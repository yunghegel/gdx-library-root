package sys.profiling

import javassist.util.proxy.ProxyFactory
import sys.Log
import sys.reflection.MethodScanner
import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class Profiler(val obj: Any) {



    companion object Factory {

        fun <T> create(type: Class<T>, argTypes: Array<Class<*>>?, argValues: Array<Any>?): T {
            val factory = ProxyFactory()
            factory.setSuperclass(type)
            factory.setFilter { m -> MethodScanner.methodHasAnnotation(m, Profile::class.java) }
            val handler = sys.profiling.MethodHandler()
            val proxy = factory.create(argTypes, argValues, handler) as T
            return proxy
        }

    }

}

object Performance {

}

class Metric(val name: String,val type: Class<*>,val method: Method) {

    var start: Long = 0
    var end: Long = 0
    var duration: Long = 0
    var calls: Int = 0

    fun start() {
        start = System.nanoTime()
    }

    fun end() {
        end = System.nanoTime()
        duration = end - start
        calls++
    }

}



