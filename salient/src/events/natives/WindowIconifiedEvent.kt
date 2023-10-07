package events.natives

import events.BaseEvent
import events.Event
import events.Subscribe

class WindowIconifiedEvent(var iconified:Boolean): Event() {

        interface Listener {
            @Subscribe
            fun  onWindowIconified(event: WindowIconifiedEvent)
        }
}