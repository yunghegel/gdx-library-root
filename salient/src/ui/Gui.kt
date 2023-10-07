package ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisSplitPane
import org.yunghegel.gdx.ui.UI
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.viewport.ViewportPanel
import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget
import app.Salient
import elemental2.dom.DomGlobal.console
import graphics.Renderer
import sys.profiling.Profile
import ui.console.Console
import ui.project.ProjectInspector
import ui.viewport.ScenePreview
import ui.viewport.SceneTabPane
import util.MiscUtils


open class Gui :Renderer {

     val skin: Skin
     val stage: Stage
     val root: STable

    lateinit var viewportWidget: ViewportWidget
    lateinit var viewportPanel : ViewportPanel
    lateinit var sceneTabPane : SceneTabPane

    lateinit var projectWidget: ProjectInspector

    lateinit var leftRightSplitPane: VisSplitPane

    init {

        UI.load()
        skin = UI.getSkin()

        if(!VisUI.isLoaded()) {
            VisUI.load(skin)
        }
        stage = Stage(ScreenViewport())
        root = STable()
        val g = Gdx.graphics as Lwjgl3Graphics
        val windowHandle = g.window.windowHandle
        createUI()
    }

    fun createUI() {
        stage.addActor(root)
        root.setFillParent(true)
        createMenuBar()
        createRenderPreview()
        createProjectWidget()
    }

    private fun createProjectWidget() {
        projectWidget = ProjectInspector()
        var projTable = STable()
        projTable.align(Align.topLeft)
        projTable.add(projectWidget).grow().row()
//        projTable.add(console).growX().row()
        leftRightSplitPane = VisSplitPane(projTable,viewportPanel, false)
        leftRightSplitPane.setSplitAmount(0.18f)
        root.add(leftRightSplitPane).grow().row()
    }

    private fun createRenderPreview() {
        viewportWidget = ViewportWidget(Salient.viewport,stage)
        viewportPanel = ScenePreview(viewportWidget)
        sceneTabPane = SceneTabPane(viewportPanel)
        viewportWidget.uiStage.addActor(viewportPanel   )
        viewportPanel.uiBody.add(sceneTabPane).growX().align(Align.topLeft).row()

//        root.add(viewportPanel).grow().row()

    }

    fun createMenuBar(){
        val menuBar = SMenuBar()
        root.add(menuBar.table).growX().row()
    }

    override fun render(delta:Float) {
        stage.act(delta)
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }



}