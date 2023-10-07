package scene.context.env

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import net.mgsx.gltf.scene3d.attributes.FogAttribute

class EditorEnvironment(config: EnvironmentData = EnvironmentData.default) {

    var config: EnvironmentData = config
    var env: Environment

    init {
        env = Environment()
        configLighting()
        configFog()
    }

    private fun configLighting(){
        env.set(ColorAttribute(ColorAttribute.AmbientLight, config.globalLighting.ambientLight, config.globalLighting.ambientLight, config.globalLighting.ambientLight, 1f))
        var directionalLight = DirectionalLight()
        directionalLight.set(config.globalLighting.directionalLight.x, config.globalLighting.directionalLight.y, config.globalLighting.directionalLight.z, config.globalLighting.directionalLight.r, config.globalLighting.directionalLight.g, config.globalLighting.directionalLight.b)

        env.add(directionalLight)
    }

    private fun configFog(){
        env.set(ColorAttribute(ColorAttribute.Fog, config.fog.r, config.fog.g, config.fog.b, config.fog.a))
        env.set(FogAttribute(FogAttribute.FogEquation).set(config.fog.near,config.fog.far,config.fog.exponent))
    }

}