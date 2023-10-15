package org.yunghegel.gdx.shaders

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider

class Providers {

    class DepthProvider : BaseShaderProvider() {

        override fun createShader(renderable: Renderable?): Shader {
            return Depth(renderable!!, DepthConfig())
        }

    }

}