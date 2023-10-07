package ui.project

import app.Salient
import events.ui.DialogChangedEvent
import org.yunghegel.gdx.ui.widgets.STable
import ui.project.dialogs.BaseInspectorDialog
import ui.project.dialogs.DialogType

object DialogContainer: STable(),DialogChangedEvent.DialogChangedListener {

    var currentDialog: BaseInspectorDialog = DialogProvider.provideUi(DialogType.EMPTY)

    init {
        add(currentDialog).grow().row()
        Salient.registerListener(this)
    }

    fun changeDialog(type: DialogType) {
        if (currentDialog.type==type) return
        currentDialog.clear()
        currentDialog.type=type
        currentDialog = DialogProvider.provideUi(type)
        currentDialog.build()
        add(currentDialog).grow().row()
    }
    fun swapDialog(container: STable) {
        container.clear()
        container.add(currentDialog).grow().row()

    }
    override fun dialogChanged(event: DialogChangedEvent) {
        clear()
       add(DialogProvider.provideUi(event.type)).grow().row()

    }




}

