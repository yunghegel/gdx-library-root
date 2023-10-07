package sys.profiling

import sys.Log
import sys.reflection.MethodScanner
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.logging.Logger


class PerfProfiler(val obj : Any) {

    private val LOGGER: Logger = Log.addLogger(PerfProfiler::class.java)

    private val methods: MutableMap<String, Method> = HashMap()

    private var target: Any? = null


    init {
        this.target = obj
        for (method in obj.javaClass.getDeclaredMethods()) {
            methods[method.name] = method
        }
    }





    @Throws(Throwable::class)
    operator fun invoke(proxy: Any?, method: Method, args: Array<Any?>): Any {
        val start = System.nanoTime()
        val result = methods[method.name]!!.invoke(target, *args)
        val elapsed = System.nanoTime() - start
        Log.info(
            String.format(
                "%s.%s: %d ns",
                target!!.javaClass.name,
                method.name,
                elapsed
            )
        )
        return result
    }

}

class MethodDelegator<T>(val obj: Any,val type:Class<T>){


}

