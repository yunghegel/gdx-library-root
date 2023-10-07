package events

import app.Salient
import com.badlogic.gdx.utils.Timer.post
import sys.OutputStream.listener

open class BaseEvent() : Event() {



    init {
        listener.eventCreated(event = this)
        println("Event Created" )
        post()
    }



   


}






