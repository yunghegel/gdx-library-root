package sys.reflection

import java.lang.reflect.Field
import java.lang.reflect.Method

object ClassScanner {

    fun getMethodsWithAnnotation(clazz: Class<*>, annotation: Class<out Annotation>): List<Method> {
        val methods = mutableListOf<Method>()
        val declaredMethods = clazz.declaredMethods
        for (method in declaredMethods) {
            val hasAnnotation = method.isAnnotationPresent(annotation)
            if (hasAnnotation) {
                methods.add(method)
            }
        }
        return methods
    }

    fun classHasAnnotation(clazz: Class<*>, annotation: Class<out Annotation>): Boolean {
        val hasAnnotation = clazz.isAnnotationPresent(annotation)
        return hasAnnotation
    }

    fun methodHasAnnotation(method: Method, annotation: Class<out Annotation>): Boolean {
        val hasAnnotation = method.isAnnotationPresent(annotation)
        return hasAnnotation
    }

    fun listMethods(clazz: Class<*>): List<Method> {
        val methods = mutableListOf<Method>()
        val declaredMethods = clazz.declaredMethods
        for (method in declaredMethods) {
            methods.add(method)
        }
        return methods
    }

    fun listFields(clazz: Class<*>): List<Field> {
        val fields = mutableListOf<Field>()
        val declaredFields = clazz.declaredFields
        for (field in declaredFields) {
            fields.add(field)
        }
        return fields
    }

    fun listConstructorParameters(clazz: Class<*>): List<Class<*>> {
        val params = mutableListOf<Class<*>>()
        val declaredConstructors = clazz.declaredConstructors
        for (constructor in declaredConstructors) {
            for (param in constructor.parameterTypes) {
                params.add(param)
            }
        }
        return params
    }

}

class MethodDescriptor


