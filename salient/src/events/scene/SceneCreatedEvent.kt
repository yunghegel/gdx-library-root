package events.scene

import events.Subscribe
import scene.Scene

class SceneCreatedEvent(val scene: Scene) {

    interface Listener {
        @Subscribe
        fun onSceneCreated(event: SceneCreatedEvent)
    }
}