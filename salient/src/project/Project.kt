package project

import app.Salient
import events.scene.SceneLoadedEvent
import events.scene.SceneSelectionEvent
import io.Serializer
import scene.Scene

class Project(val name: String, val path: String,val uid:String) {

    var scenes: MutableList<Scene> = mutableListOf()
    var currentScene: Scene? = null


    fun setup(){
        if(scenes.isEmpty()) {
            scenes.add(Scene.createDefault(this))
        }
        loadScene(scenes[0])
    }

    fun addScene(scene: Scene) {

        scenes.add(scene)
        save()
    }

    fun removeScene(scene: Scene) {
        scenes.remove(scene)
    }

    fun loadScene(scene: Scene) {
        currentScene = scene
        Salient.postEvent(SceneLoadedEvent(scene))

    }

    fun setScene(scene: Scene){
        currentScene = scene
        Salient.postEvent(SceneSelectionEvent(scene))
    }

    fun save() {
        Serializer.serializeProject(this)
        Serializer.serializeTmpData()
    }

}