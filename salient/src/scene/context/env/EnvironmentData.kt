package scene.context.env

import kotlinx.serialization.Serializable

@Serializable
data class GlobalLightingData(val ambientLight: Float, val directionalLight: DirectionalLightData) {
    companion object {
        val default = GlobalLightingData(0.5f, DirectionalLightData.default)
    }
}
@Serializable
data class DirectionalLightData(val x:Float, val y: Float,val z:Float, val r:Float,val g:Float,val b:Float,val a:Float) {
    companion object {
        val default = DirectionalLightData(-.8f, .2f, .4f, 1f, 1f, 1f, 1f)
    }
}
@Serializable
data class FogData(val r:Float,val g:Float,val b:Float,val a:Float, val exponent: Float,val near:Float, val far:Float) {
    companion object {
        val default = FogData(0.5f, 0.5f, 0.5f, 1f, 0.01f,0.1f,100f)
    }
}
@Serializable
data class EnvironmentData(val globalLighting: GlobalLightingData, val fog: FogData) {
    companion object {
        val default = EnvironmentData(GlobalLightingData.default, FogData.default)
    }
}