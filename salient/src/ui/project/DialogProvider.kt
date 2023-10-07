package ui.project

import app.Salient
import com.badlogic.gdx.files.FileHandle
import events.ui.DialogChangedEvent
import io.DirectoryMappings
import ktx.inject.Context
import ktx.inject.register
import ktx.reflect.Reflection
import ui.project.DialogProvider.state
import ui.project.dialogs.*

object DialogProvider {

    private val context: Context =Salient.context
    private lateinit var old : DialogType

    var state = DialogType.EMPTY
        set(value) {
            watch(state, value)
            field = value
        }


    init {
        create()
    }



    fun watch(old: DialogType, new: DialogType) {
        if (old !=new) {
            println("Dialog changed from $old to $new")
            Salient.postEvent(DialogChangedEvent(new))
        }
    }
    @OptIn(Reflection::class)
    private fun create(){
        context.register {
            bindSingleton<EmptyDialog>(EmptyDialog())
            bindSingleton<ProjectDialog>(ProjectDialog())
            bindSingleton<SceneDialog>(SceneDialog())
            bindSingleton<ConfigDialog>(ConfigDialog())
            bindSingleton<AssetsDialog>(AssetsDialog())
            bindSingleton<LogDialog>(LogDialog())
        }
    }

    fun setDialogType(type: DialogType) {
        state = type
    }

    fun provideUi(type: DialogType): BaseInspectorDialog {
        return when (type) {
            DialogType.EMPTY -> context.inject<EmptyDialog>()
            DialogType.PROJECT -> ProjectDialog()
            DialogType.SCENE -> context.inject<SceneDialog>()
            DialogType.CONFIG -> context.inject<ConfigDialog>()
            DialogType.ASSETS -> context.inject<AssetsDialog>()
            DialogType.LOG -> context.inject<LogDialog>()
        }
    }

    fun matchPath(file:FileHandle) :DialogType {
        println(file.name())
        return when (file.name()) {
            "projects"-> DialogType.PROJECT
            "scenes" -> DialogType.SCENE
            "salient.config" -> DialogType.CONFIG
            "assets" -> DialogType.ASSETS
            "salient.log" -> DialogType.LOG

            else -> matchHierarchy(file)

        }
    }

    fun matchHierarchy(file:FileHandle) :DialogType {
        println(file.name())
        return when (file.parent().name()) {
            "projects"-> DialogType.PROJECT
            "scenes" -> DialogType.SCENE
            "assets" -> DialogType.ASSETS
            else -> DialogType.EMPTY

        }
    }


    fun swapDialog(container: DialogContainer) {


    }




}

