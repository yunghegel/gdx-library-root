package ui.project

import app.Salient
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Tree
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Selection
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisSplitPane
import events.proj.ProjectLoadedEvent
import events.ui.DialogChangedEvent
import io.DirectoryMappings
import net.dermetfan.gdx.scenes.scene2d.ui.FileChooser
import net.dermetfan.gdx.scenes.scene2d.ui.ListFileChooser
import net.dermetfan.gdx.scenes.scene2d.ui.TreeFileChooser
import net.dermetfan.utils.Function
import org.yunghegel.gdx.ui.UI
import org.yunghegel.gdx.ui.widgets.EditableTextButton
import org.yunghegel.gdx.ui.widgets.STable
import sys.Log
import ui.project.dialogs.DialogType
import ui.project.dialogs.ProjectDialog
import ui.project.dialogs.SceneDialog
import java.io.File
import java.io.FileFilter
import java.util.function.Supplier

class ProjectInspector :STable(), ProjectLoadedEvent.Listener, DialogChangedEvent.DialogChangedListener{

    lateinit var fileChooser : ui.project.FileChooser
    val projDialog = STable()
    val dialogContainer = DialogContainer

    var listFileChooser :ListFileChooser
    var projLoaded = false
    init {
        Salient.registerListener(this)


        fileChooser = FileChooser(FileBrowserListener(), FileBrowserFilter(),width)
        fileChooser.fileFilter = (FileBrowserFilter())
        var style = UI.getSkin().get("default",ListFileChooser.Style::class.java)
        listFileChooser = object: ListFileChooser(skin,"default",FileBrowserListener()) {
            override fun layout() {
                if(super.getContents().items.size < 20) {
                    val idx=20-super.getContents().items.size
                    for (i in 0..idx) {
                        super.getContents().items.add("- ")
                    }
                    super.setContents(super.getContents())
                }
            }
        }

        listFileChooser.fileFilter = ListFilter { return@ListFilter FileHandle(fileChooser.getPath())   }

        listFileChooser.directory = Gdx.files.getFileHandle(DirectoryMappings.PROJECTS_DIR, Files.FileType.Absolute)

        fileChooser.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (fileChooser.tree.getSelection() != null) {
                    val selection: Selection<*>? = fileChooser.tree.getSelection()
                    if (selection?.getLastSelected() is Tree.Node<*,*,*>) {
                        val node = selection.getLastSelected() as Tree.Node<*,*,*>
                        if (node.value is FileHandle) {
                            val file = node.value as FileHandle
                            if (file.isDirectory()) {
                                listFileChooser.directory = file
                                listFileChooser.setDirectory(   file)
                                listFileChooser.fileFilter = ListFilter { return@ListFilter FileHandle(fileChooser.getPath())   }
                            }
                            var type = DialogProvider.matchPath(file)
                            DialogProvider.state = type
                        }
                    }
                }
            }
        })
        fileChooser.fileSelectListener = ui.project.FileChooser.FileSelectListener { file -> listFileChooser.setDirectory(file) }

        Salient.eventBus.register(this)
        align(Align.top)
        val pathTable = STable()

        val textButton = TextButton("/", UI.getSkin())

        pathTable.add(textButton).height(20f).padLeft(0f).padRight(0f)
        pathTable.add(fileChooser.path).growX().height(20f).padLeft(2f).padRight(2f).row()
        var fileChooserTable = STable()
        var listContainer = STable()
        listContainer.add(listFileChooser.contentsPane).growY().padLeft(0f).padRight(0f).fill().row()
        val treeContainer = STable()

        treeContainer.add(fileChooser.treePane).growY().padLeft(0f).padRight(0f).fill().row()
        val pane = VisSplitPane(treeContainer, listContainer, false)
        val Separator = Separator()
        val projWindow = Window("Explorer", UI.getSkin())
        add(pathTable).growX().padBottom(5f).padTop(5f).padLeft(4f).padRight(4f).row()
       fileChooserTable.add(pane).pad(0f).grow().fillY().height(300f).row()
        add(fileChooserTable).growX().fill().row()

        var table = STable()


        var buttonsTable = STable()
        buttonsTable.add(fileChooser.chooseButton).left().padLeft(5f).padRight(5f).fillX().growX()
        buttonsTable.add(fileChooser.cancelButton).right().padLeft(5f).padRight(5f).fillX().growX().row()
//        table.add(projWindow).growX().fillX().row()
        add(Separator).colspan(2).growX().row()
        treeContainer.add(buttonsTable).growX().fillX().height(25f).row()

        var titleButtons = STable()
        var refresh = TextButton("Refresh",UI.getSkin())
        refresh.addListener {
            fileChooser.configureNodes()
             true
        }
        titleButtons.add(refresh).left().fillX().grow().padLeft(5f).padRight(5f);
        titleButtons.add(TextButton("Upload Assets",UI.getSkin())).fillX().grow().right();

            titleButtons.align(Align.center)
        listContainer.add(titleButtons).growX().fillX().height(25f).row()

        add(DialogContainer).growX().row()
        add(Separator).colspan(2).growX().row()

    }



    class FileBrowserListener : FileChooser.Listener {

        override fun choose(file: FileHandle?) {
            if (file!!.extension()==".salient") {

            } else {
                Log.info("load file")
            }
            if (file.extension()==".scene") {
                Log.info("load scene")
            }

        }

        override fun choose(files: Array<FileHandle>?) {
            println("load files")
        }

        override fun cancel() {
        }

    }

    class FileBrowserFilter : FileFilter {
        var validExtensions = listOf(".config",".log")
        override fun accept(pathname: File?): Boolean {
            if (pathname == null) return false
            if (pathname.isDirectory) return true
            if(pathname.path.endsWith('-')) return true
            if (pathname.extension == "") return true
            if (validExtensions.contains(pathname.extension)) return true
            return false
        }

    }

    class ListFilter(val getter: Supplier<FileHandle>) : FileFilter {

        override fun accept(pathname: File?): Boolean {
            if (getter.get().file().path == pathname!!.path) return true
            return true
        }

    }

    override fun onProjectLoaded(event: ProjectLoadedEvent) {
        var project= event.project
if(!projLoaded){
    val supplier =
        Function<FileHandle, Label> { file: FileHandle ->
            val label = EditableTextButton(file.name())
            if (file.isDirectory()) {
                label.label.label.text.append("/")
                if (file.name() == "projects") label.label.label.color =
                    Color.GOLD else if (file.name() == "assets") label.label.label.color =
                    Color.SKY else if (file.name() == "scenes") label.label.label.color =
                    Color.FOREST
                label.label.label.setStyle(skin.get("default-medium", LabelStyle::class.java))
            } else {
                label.color = Color.WHITE
                label.label.label.setStyle(skin.get("default-small", LabelStyle::class.java))
            }
            label.label.label
        }
    val node = TreeFileChooser.fileNode(FileHandle(DirectoryMappings.getProjectPath(project.name)),
        FileBrowserFilter(),supplier)
    projLoaded = true
    fileChooser.tree.add(node)
}

//        var node = TreeFileChooser.fileNode(FileHandle(DirectoryMappings.getProjectPath(project.name)),FileBrowserFilter(),UI.getSkin().get("default",LabelStyle::class.java))
//        var scenes = fileChooser.add(Gdx.files.getFileHandle(DirectoryMappings.getScenesPath(project.name),Files.FileType.Absolute))

    }

    override fun dialogChanged(event: DialogChangedEvent) {

    }

}