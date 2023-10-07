package sys

import app.Salient
import events.lifecycle.ShutdownEvent
import events.lifecycle.StartupEvent
import events.natives.WindowFocusEvent
import events.natives.WindowIconifiedEvent
import events.natives.WindowMaximizedEvent

open class WindowSubscriber : WindowMaximizedEvent.Listener, WindowIconifiedEvent.Listener, WindowFocusEvent.FocusLostListener, WindowFocusEvent.FocusGainedListener,StartupEvent.Listener, ShutdownEvent.Listener {





init {
    }

    override fun onFocusLost(event: WindowFocusEvent) {
        println("Window focus event")
        event.listener.eventPosted(event)

    }

    override fun  onFocusGained(event: WindowFocusEvent) {
        event.listener.eventPosted(event)
    }


    override fun  onWindowIconified(event: WindowIconifiedEvent) {
        println("Window iconified event")
        event.listener.eventPosted(event)

    }

    override fun onWindowMaximized(event: WindowMaximizedEvent) {
        Log.info("Window maximized event")
        event.listener.eventPosted(event)

    }

    override fun onStartup(event: StartupEvent) {

        Log.info("Startup event")
        event.listener.eventHandled(event)

    }
    override fun onShutdown(event: ShutdownEvent) {
        event.listener.eventHandled(event)
    }
}