package scene

import app.Salient
import events.scene.SceneCreatedEvent
import events.scene.SceneLoadedEvent
import events.scene.SceneSelectionEvent
import io.Serializer
import project.Project
import scene.context.env.EditorEnvironment
import util.CameraUtils

class SceneManager(project: Project) : SceneLoadedEvent.Listener, SceneCreatedEvent.Listener, SceneSelectionEvent.Listener {

    private var proj = project
    private lateinit var currentScene: Scene

    init {
        Salient.eventBus.register(this)
    }

    fun setScene(scene: Scene){
        currentScene = scene
        if(!proj.scenes.contains(scene)){
            proj.addScene(scene)
            proj.save()
            proj.setScene(scene)
        }
    }

    fun closeScene(){
        saveScene()
        currentScene.dispose()
    }

    fun saveScene(){
        Serializer.serializeScene(currentScene)
    }

    fun newScene(name:String,makeCurrent: Boolean?) : Scene {
        var scene = Scene(name, proj)
        scene.env = EditorEnvironment()
        scene.camera= Salient.camera
        proj.addScene(scene)
        Serializer.serializeScene(scene)
        if(makeCurrent == true){
            setScene(scene)
        }
        Salient.postEvent(SceneCreatedEvent(scene))
        return scene
    }




    fun initDefaultScene(): Scene {
       return newScene("New Scene",true)
    }

    override fun onSceneLoaded(event: SceneLoadedEvent) {
//        Salient.cameraController.perspectiveCamera=(event.scene.camera)

    }

    override fun onSceneCreated(event: SceneCreatedEvent) {
        Serializer.serializeScene(event.scene)
        proj.addScene(event.scene)
        proj.save()
    }

    override fun onSceneSelection(event: SceneSelectionEvent) {
        currentScene.save()

        var lookAt = CameraUtils.cameraRayXZPlaneIntersection(event.scene.camera.position, event.scene.camera.direction)
//        Salient.cameraController.perspectiveCameraController.config.target.(lookAt)

        Salient.ui.viewportWidget.setRenderer(event.scene)
        setScene(event.scene)
    }


}