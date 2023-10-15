package org.yunghegel.gdx.shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader

class Depth(renderable: Renderable,config:DepthConfig) : DefaultShader(renderable, config) {

    val vertexShader: String = config.vertexShader
    val fragmentShader: String = config.fragmentShader

}

class DepthConfig: DefaultShader.Config() {

    init {
        vertexShader = getVertexShader()
        fragmentShader = getFragmentShader()
    }

    fun getVertexShader(): String {
        return Gdx.files.internal("depth.vert").readString()
    }

    fun getFragmentShader(): String {
        return Gdx.files.internal("depth.frag").readString()
    }

}