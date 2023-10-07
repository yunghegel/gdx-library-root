package scene

import kotlinx.serialization.Serializable
import scene.context.env.CameraData
import scene.context.env.EnvironmentData

@Serializable
data class SceneData(val name:String, val path:String, val uid:String, val environment: EnvironmentData, val cam: CameraData)