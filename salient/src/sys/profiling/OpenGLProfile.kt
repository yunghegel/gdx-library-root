package sys.profiling

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler

object OpenGLProfile  {

    val glProfiler: GLProfiler = GLProfiler(Gdx.graphics)

    fun enable() {
        glProfiler.enable()
    }

    fun reset() {
        glProfiler.reset()
    }

    fun getDrawCalls(): Int {
        return glProfiler.drawCalls
    }

    fun getTextureBindings(): Int {
        return glProfiler.textureBindings
    }

    fun getShaderSwitches(): Int {
        return glProfiler.shaderSwitches
    }

    fun getVertexCount(): Float {
        return glProfiler.vertexCount.total
    }

    fun getRenderCalls(): Int {
        return glProfiler.calls
    }

    override fun toString(): String {
        return "OpenGLProfiler(drawCalls=${getDrawCalls()}, textureBindings=${getTextureBindings()}, shaderSwitches=${getShaderSwitches()}, vertexCount=${getVertexCount()}, renderCalls=${getRenderCalls()})"
    }

}