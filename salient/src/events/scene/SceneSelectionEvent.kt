package events.scene

import events.Subscribe
import scene.Scene

class SceneSelectionEvent(val scene: Scene) {

    interface Listener {
        @Subscribe
        fun onSceneSelection(event: SceneSelectionEvent)
    }
}