package sys.reflection

import java.lang.reflect.Method

object MethodScanner {

    fun methodHasAnnotation(method: Method, annotation: Class<out Annotation>): Boolean {
        return method.isAnnotationPresent(annotation)
    }

    fun getMethodsWithAnnotation(clazz: Class<*>, annotation: Class<out Annotation>): List<Method> {
        val methods = mutableListOf<Method>()
        val declaredMethods = clazz.declaredMethods
        for (method in declaredMethods) {
            val hasAnnotation = method.isAnnotationPresent(annotation)
            if (hasAnnotation) {
                methods.add(method)
            }
        }
        println("Found ${methods.size} methods with annotation ${annotation.simpleName}")
        return methods
    }

    fun listParameters(method: Method): List<Class<*>> {
        val parameters = mutableListOf<Class<*>>()
        val parameterTypes = method.parameterTypes
        for (parameterType in parameterTypes) {
            parameters.add(parameterType)
        }
        return parameters
    }

}