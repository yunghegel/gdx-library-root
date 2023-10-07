package events

import app.Salient
import sys.Log

open class Event()  {



    val desc=""
    var listener: DefaultLogger = Salient.eventLogger




    init {
        listener.eventCreated(event = this)
        Log.info("creating event: ${this::class.java.simpleName}")
    }

    fun post() {
        listener.eventPosted(event = this)
    }

    fun handle(): Boolean {
        return listener.eventHandled(event = this)
    }

    interface Listener {
        fun eventCreated(event: Event)
        fun eventPosted(event: Event)
        fun eventHandled(event: Event): Boolean
    }
}

enum class Type {
    IO,NATIVE,PROJECT,SCENE
}

