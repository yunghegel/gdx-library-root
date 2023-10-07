package project

import app.Salient
import events.natives.WindowFocusEvent
import sys.Log
import events.proj.ProjectCreatedEvent
import events.proj.ProjectLoadedEvent
import io.DirectoryMappings
import io.FileService
import io.Serializer
import scene.SceneManager
import sys.profiling.Profile

class ProjectManager
{
    lateinit var currentProject: Project

    lateinit var sceneManager: SceneManager

     lateinit var tmpData: TmpData

    val recentProjects: List<String>
        get() = tmpData.recentProjects

    init {
    }



    @Profile
    fun initialize(name: String){
        currentProject = load(name)

        sceneManager = SceneManager(currentProject)


        Log.info("Initializing project $name")
        FileService.listScenes(name).forEach {
            if (currentProject.scenes.find { scene -> scene.name == it.substringBefore('.') } == null) {
                currentProject.scenes.add(Serializer.deserializeScene(name, it.substringBefore('.'), currentProject))
            }

        }
        if(currentProject.scenes.isEmpty()) {
            Log.info("No scenes found, creating default scene")
            sceneManager.initDefaultScene()
        }
        currentProject.loadScene(currentProject.scenes[0])
        if(currentProject.currentScene == null){
            Log.info("No scene loaded, loading first scene")
            currentProject.loadScene(currentProject.scenes[0])
        }
        Log.info("Retreived scene ${currentProject.currentScene!!.name} from project ${currentProject.name}")

        tmpData = Serializer.deserializeTmpData()

        Salient.postEvent(ProjectLoadedEvent(currentProject))
        Salient.registerListener(listener)
    }

    @Profile
    fun save(){
        currentProject.save()
        Serializer.serializeTmpData()
        Log.info("Saved project ${currentProject.name}")
    }

    @Profile
    fun load(name:String): Project {
        Log.info("Loading project $name")

        if(FileService.fileExists(DirectoryMappings.getProjectPath(name))) {
            Log.info("Project $name exists, retrieving from disk")
            var project = Serializer.deserializeProject(name)
            Salient.postEvent(ProjectLoadedEvent(project))

            return project
        }
        else{
            Log.info("Project $name does not exist, creating new project")
            var project = create(name)
            Salient.postEvent(ProjectLoadedEvent(project))
            return project
        }

    }

    @Profile
    fun create(name:String): Project {
        var project = Project(name, DirectoryMappings.getProjectPath(name), Serializer.generateUID())
        project.setup()
        FileService.createAssetFolders(name)
        Serializer.serializeProject(project)
        TmpData.appendRecentProject(name)
        currentProject = project
        Salient.postEvent(WindowFocusEvent(true))
        Salient.postEvent(ProjectCreatedEvent(project))
        return project
    }



    object listener: ProjectLoadedEvent.Listener {
        override fun onProjectLoaded(event: ProjectLoadedEvent) {
            Salient.projectManager.tmpData = Serializer.deserializeTmpData()
        }
    }

}