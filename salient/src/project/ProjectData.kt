package project

import kotlinx.serialization.Serializable
import io.Serializer
import scene.SceneData
import java.util.Date

@Serializable
data class ProjectData(val name:String, val path:String,val uid:String, val scenes:List<String>)

@Serializable
data class TmpData(var mostRecentProject:String ="default", var mostRecentScene:String ="New Scene", var recentProjects:List<String> = mutableListOf("New Scene")) {
    companion object {
        fun appendRecentProject(name:String){
            val tmp = Serializer.deserializeTmpData()
            tmp.recentProjects = tmp.recentProjects.filter { it != name }
            tmp.recentProjects = tmp.recentProjects.plus(name)
            Serializer.serializeTmpData()
        }
    }


}
