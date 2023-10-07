package events.proj

import events.Subscribe
import project.Project

class ProjectChangedEvent(project: Project) {

        interface Listener {
            @Subscribe
            fun onProjectChanged(event: ProjectChangedEvent)
        }
}