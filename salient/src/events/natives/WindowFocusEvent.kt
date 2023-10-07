package events.natives

import events.BaseEvent
import events.Event
import events.Subscribe

class WindowFocusEvent(var focused:Boolean) : Event() {

    interface FocusLostListener {
        @Subscribe
        fun onFocusLost(event: WindowFocusEvent)
    }


    interface FocusGainedListener {
        @Subscribe
        fun onFocusGained(event: WindowFocusEvent)
    }


}
