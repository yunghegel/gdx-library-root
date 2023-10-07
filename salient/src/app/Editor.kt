package app

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT
import com.badlogic.gdx.math.Vector3
import project.ProjectManager
import util.CameraUtils


class Editor : ApplicationAdapter() {

    lateinit var projectManager: ProjectManager

    override fun create() {
        super.create()
        projectManager = Salient.inject()
        Salient.initializeProjectContext()

    }

    override fun render() {
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        Gdx.gl.glClearColor(0.1f,0.1f,0.1f,0f)
        projectManager.currentProject.currentScene?.update(Gdx.graphics.deltaTime)

        Salient.ui.render(Gdx.graphics.deltaTime)
        with(Salient.debugDrawer) {
            renderer.projectionMatrix = projectManager.currentProject.currentScene!!.camera.combined
            renderer.setAutoShapeType(true)
            var intersection = CameraUtils.cameraRayXZPlaneIntersection(
                projectManager.currentProject.currentScene!!.camera.position,
                projectManager.currentProject.currentScene!!.camera.direction)
            if (intersection != null) {
                renderer.begin()
                renderer.color = Color.CORAL
                renderer.line(intersection, Vector3(intersection.x, 0f, intersection.z))
                renderer.end()

            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        projectManager.currentProject.currentScene?.resize(width, height)
        Salient.ui.resize(width, height)

    }

    inline fun <reified> clearColorAndDepthBuffer() {
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    inline fun <reified> clearColor() {
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f,0f)
    }

}