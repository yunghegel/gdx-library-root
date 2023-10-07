package events.natives

import events.BaseEvent
import events.Event
import events.Subscribe

class WindowMaximizedEvent (val isMaximized: Boolean) : Event() {

    interface Listener {
        @Subscribe
        fun onWindowMaximized(event: WindowMaximizedEvent)
    }
}