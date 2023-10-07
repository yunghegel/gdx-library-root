package events.ui

import events.Event
import events.Subscribe
import ui.project.dialogs.DialogType

class DialogChangedEvent(val type:DialogType):Event() {


    interface DialogChangedListener {
        @Subscribe
        fun dialogChanged(event: DialogChangedEvent)
    }

}