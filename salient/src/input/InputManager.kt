package input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor

class InputManager {

    val inputMultiplexer = InputMultiplexer()

    init {
        Gdx.input.inputProcessor = inputMultiplexer
    }


    fun addProcessor(processor: InputProcessor) {
        inputMultiplexer.addProcessor(processor)
    }

    fun removeProcessor(processor: InputProcessor) {
        inputMultiplexer.removeProcessor(processor)
    }

    fun clear() {
        inputMultiplexer.clear()
    }

    fun setProcessor(processor: InputProcessor) {
        Gdx.input.inputProcessor = processor
    }

    fun pauseInput(){
        Gdx.input.inputProcessor = null
    }

    fun resumeInput(){
        Gdx.input.inputProcessor = inputMultiplexer
    }

    fun pauseInput(vararg processors: InputProcessor) {
        Gdx.input.inputProcessor = null
        inputMultiplexer.setProcessors(*processors)
    }

    fun setProcessors(vararg processors: InputProcessor) {
        inputMultiplexer.setProcessors(*processors)
    }

    fun keyDown(keycode: Int): Boolean {
        return inputMultiplexer.keyDown(keycode)
    }

    fun keyUp(keycode: Int): Boolean {
        return inputMultiplexer.keyUp(keycode)
    }

    fun keyTyped(character: Char): Boolean {
        return inputMultiplexer.keyTyped(character)
    }

    fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button)
    }

    fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button)
    }

    fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer)
    }

    fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return inputMultiplexer.mouseMoved(screenX, screenY)
    }







}