package events.proj

import events.Event
import events.Subscribe
import project.Project

class ProjectLoadedEvent(val project: Project) :Event() {


    interface Listener {
        @Subscribe
        fun onProjectLoaded(event: ProjectLoadedEvent)
    }
}