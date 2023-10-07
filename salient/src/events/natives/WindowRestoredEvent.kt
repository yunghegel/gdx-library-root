package events.natives

import events.Event
import events.Subscribe

class WindowRestoredEvent : Event() {

    interface Listener {
        @Subscribe
        fun onWindowRestored(event: WindowRestoredEvent)
    }

}