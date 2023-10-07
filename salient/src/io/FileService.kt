package io

import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import java.io.File

object FileService {



    init {
       val prettyPrint = Json { prettyPrint = true }
    }

    fun getOrCreateFile(path:String):FileHandle{
        val file = FileHandle(path)
        if(!file.file().exists()){
            var file = File(path)
            if(!file.parentFile.exists()){
                file.parentFile.mkdirs()
            }
            file.createNewFile()
        }
        return file
    }

    fun createDirectoryIfNotExists(path:String){
        val file = FileHandle(path)
        if(!file.exists()){
            file.file().mkdirs()
        }
    }

    fun fileExists(path:String):Boolean{
        return FileHandle(path).exists()
    }

    fun writeStringToFile(path:String, content:String){

        val file = getOrCreateFile(path)
        file.writeString(content, false)
    }

    fun getProjectFile(projectName:String):FileHandle{
        return getOrCreateFile(DirectoryMappings.getProjectPath(projectName))
    }

    fun getSceneFile(projectName:String, sceneName:String):FileHandle{
        return getOrCreateFile(DirectoryMappings.getScenePath(projectName, sceneName))
    }

    fun getPreferencesFile():FileHandle{
        return getOrCreateFile(DirectoryMappings.CONFIG_FILEPATH)
    }

    fun getTmpFile():FileHandle{
        return getOrCreateFile(DirectoryMappings.TMP_FILEPATH)
    }

    fun getLogFile():FileHandle{
        return getOrCreateFile(DirectoryMappings.LOG_FILEPATH)
    }

    fun listProjects():List<String>{
        return FileHandle(DirectoryMappings.PROJECTS_DIR).list().map { it.nameWithoutExtension() }
    }

    fun listScenes(projectName:String):List<String>{
        return FileHandle(DirectoryMappings.getScenesPath(projectName)).list().map { it.nameWithoutExtension() }
    }

    fun createAssetFolders(projectName:String){
        createDirectoryIfNotExists(DirectoryMappings.getAssetsPath(projectName))
        createDirectoryIfNotExists(DirectoryMappings.getAssetsPath(projectName) +"/models")
        createDirectoryIfNotExists(DirectoryMappings.getAssetsPath(projectName) +"/textures")
        createDirectoryIfNotExists(DirectoryMappings.getAssetsPath(projectName) +"/shaders")
    }

}