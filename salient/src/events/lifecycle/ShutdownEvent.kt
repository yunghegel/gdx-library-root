package events.lifecycle

import events.BaseEvent
import events.Event
import events.Subscribe

class ShutdownEvent : Event() {

    interface Listener {

        @Subscribe
        fun onShutdown(event: ShutdownEvent)
    }

}