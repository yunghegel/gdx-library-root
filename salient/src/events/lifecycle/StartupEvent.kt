package events.lifecycle

import events.*

class StartupEvent  : Event() {

    interface Listener {

        @Subscribe
        fun  onStartup(event: StartupEvent)
    }

}