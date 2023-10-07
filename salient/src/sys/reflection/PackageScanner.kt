package sys.reflection

import java.io.File

object PackageScanner {

    fun scanPackage(packageName: String): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path)
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            val file = File(resource.file)
            val directory = file.isDirectory
            if (directory) {
                val files = file.listFiles()
                for (child in files!!) {
                    val name = child.name
//                    check if it's a .java file, or a directory
                    val isJavaFile = name.endsWith(".class")
                    if (isJavaFile) {
                        val className = name.substring(0, name.length - 6)
                        val qualifiedName = "$packageName.$className"
                        val clazz = Class.forName(qualifiedName)
                        classes.add(clazz)
                    }
//                    if its not a java file, it's another package
                    else {
                        val childPackageName = "$packageName.$name"
                        val childClasses = scanPackage(childPackageName)
                        classes.addAll(childClasses)
                    }
                }
            }
        }
        return classes
    }

    fun scanPackage(packageName: String, callback: ClassScannedCallback) : List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path)
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            val file = File(resource.file)
            val directory = file.isDirectory
            if (directory) {
                val files = file.listFiles()
                for (child in files!!) {
                    val name = child.name
//                    check if it's a .java file, or a directory
                    val isJavaFile = name.endsWith(".class")
                    if (isJavaFile) {
                        val className = name.substring(0, name.length - 6)
                        val qualifiedName = "$packageName.$className"
                        val clazz = Class.forName(qualifiedName)
                        classes.add(clazz)
                        callback.onClassScanned(clazz)
                    }
//                    if its not a java file, it's another package
                    else {
                        val childPackageName = "$packageName.$name"
                        val childClasses = scanPackage(childPackageName,callback)
                        classes.addAll(childClasses)

                    }
                }
            }
        }
        return classes
    }


}

interface ClassScannedCallback {

    fun onClassScanned(clazz: Class<*>)

}





fun main(args: Array<String>) {
    val scanner = PackageScanner
    val classes = PackageScanner.scanPackage("scene", object : ClassScannedCallback {
        override fun onClassScanned(clazz: Class<*>) {
            println(clazz)
        }
    })


}