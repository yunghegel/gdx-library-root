package events.proj

import events.Subscribe
import project.Project

class ProjectCreatedEvent(val project: Project) {

    interface Listener {
        @Subscribe
        fun onProjectCreated(event: ProjectCreatedEvent)
    }
}