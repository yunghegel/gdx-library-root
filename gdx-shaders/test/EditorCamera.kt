

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Plane
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.Ray
import com.badlogic.gdx.utils.viewport.ScreenViewport

class EditorCamera(var perspectiveCamera: PerspectiveCamera,var homeTarget: Vector3,var homeDistance: Float) {

    var pCam = perspectiveCamera
    var orthographicCamera = OrthographicCamera()
    var lerpAmount = 0.5f
    init {
        orthographicCamera.position.set(10f, 10f, 0f)
        orthographicCamera.lookAt(0f, 0f, 0f)
        orthographicCamera.near = 10f
        orthographicCamera.far = 150f
        orthographicCamera.update()

        perspectiveCamera.position.set(1f, 1f, 1f)
        perspectiveCamera.lookAt(0f, 0f, 0f)
        perspectiveCamera.near = 1f
        perspectiveCamera.far = 100f
        perspectiveCamera.update()

    }

    fun setEnabled(enabled: Boolean){
        perspectiveCameraController.enabled=enabled
    }

    val perspectiveCameraController: PerspectiveCameraController = PerspectiveCameraController(perspectiveCamera, viewport = ScreenViewport(perspectiveCamera),config = Config(target = Vector3(0f, 0f, 0f), distanceToHome = homeDistance, homeTarget = homeTarget))

    data class Config(var velocity: Float =30f, val target: Vector3 = Vector3(0f,0f,0f), var distanceToHome: Float=10f, var homeTarget: Vector3 = Vector3(0f,0f,0f),var lerp: Float=0.5f,var translateUnits:Float =30f)
    data class Keybinds(var rotate:Int= Input.Buttons.RIGHT, var pan:Int=Input.Buttons.MIDDLE, var zoom:Int=Input.Buttons.FORWARD, var home:Int=Input.Keys.CONTROL_LEFT,var reset:Int=Input.Keys.PERIOD,var button:Int=Input.Buttons.RIGHT)

    abstract class CameraController(var camera: PerspectiveCamera) : InputAdapter(),GestureListener {

        var keybinds = Keybinds()
        var cam = camera
        var tmp : Keybinds = keybinds.copy()
        var changed: Boolean = false

        abstract fun update(delta: Float)



        fun pauseInput(){
            tmp=keybinds.copy()
            keybinds.pan = -1
            keybinds.rotate = -1
            keybinds.zoom = -1
            keybinds.home = -1
        }

        fun resumeInput(){
            keybinds=tmp.copy()
        }



    }


    inner class PerspectiveCameraController(camera: PerspectiveCamera,var viewport: ScreenViewport,var config:Config) : CameraController(camera) {


        val rayCaster = object : Raycaster.Raycast {
            override fun castRay(ray: Ray, pos: Vector3, callback: Raycaster.RaycastCallback): Raycaster.RaycastResult {
                var result = Raycaster.RaycastResult()
                var plane = Plane(Vector3.Y, 0f)
                if (Intersector.intersectRayPlane(ray, plane, result.pos)) {
                    callback.onRaycast(Raycaster.RaycastResult(true, pos, Vector3.Y))
                } else {
                    callback.onRaycast(Raycaster.RaycastResult(false, pos, Vector3.Y))
                }
                return result
            }
        }
        val raySupplier = object : Raycaster.SupplyRay {
            override fun supplyRay(x: Float, y: Float): Ray {
                return viewport.getPickRay(x, y)
            }
        }

        var tangent = Vector3()
        var prevTarget= Vector3()
        var prevPosition= Vector3()
        var movePosition = Vector3()
        var oldCameraDir = Vector3()
        var oldCameraPos = Vector3()


        var tmpV1 = Vector3()
        var tmpV2 = Vector3()

        var moveTarget: Vector3? = null
        var target = Vector3(config.target.x, config.target.y, config.target.z)
        var homeTarget= Vector3(config.homeTarget.x,config.homeTarget.y,config.homeTarget.z)

        var time = 0f
        var homeDistance = config.distanceToHome
        var velocity = config.velocity
        var pinchZoomFactor = 10f
        var translateUnits = 30f
        var scrollFactor = -0.1f
        var previousZoom=0f
        var rotateAngle = 360f
        var startX = 0f
        var startY = 0f

        var enabled = true

        init {
            camera.position.set(config.homeTarget).nor().scl(config.homeTarget.cpy().scl(-1f)).add(config.homeTarget)
            camera.up.set(Vector3.Y)
            camera.lookAt(target)
            camera.update()
        }

        override fun update(delta:Float){
            if(!enabled) return
            target.y=0f
            if (Gdx.input.isKeyPressed(keybinds.reset)) {
            moveTarget = Vector3(config.homeTarget)
            prevTarget.set(target)
            prevPosition.set(camera.position)
            movePosition = Vector3().set(config.homeTarget).sub(prevPosition).nor().scl(-config.distanceToHome).add(config.homeTarget)
            time = 0f
        }
            if(moveTarget!=null) {

                time += delta*2f
                target.set(prevTarget).lerp(moveTarget, time)
                camera.position.set(prevPosition).lerp(movePosition, time)
                if (time >= 1f) {
                    moveTarget = null
//                    keybinds.pan = tmp.pan
//                    keybinds.rotate = tmp.rotate
//                    keybinds.zoom = tmp.zoom
//                    keybinds.home = tmp.home
                }
                camera.up.set(Vector3.Y)
                camera.lookAt(target)
                changed = true
            }

            tangent.set(camera.direction).crs(camera.up).nor()
            translateUnits = cam.position.dst(target)


        }

        fun process(keycode:Int, deltaX: Float,deltaY: Float) {
            if(!enabled) return
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                tmpV1.set(camera.direction).crs(camera.up).y = 0f
                lerpRotateAround(target, tmpV1.nor(), deltaY * rotateAngle,config.lerp  )
                lerpRotateAround(target, Vector3.Y, deltaX * -rotateAngle,config.lerp)
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                camera.translate(tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * config.translateUnits))
                camera.translate(tmpV2.set(camera.up).scl(-deltaY * config.translateUnits))
                target.add(tmpV1).add(tmpV2)
            }
            if (Gdx.input.isButtonPressed(keybinds.zoom)) {
                camera.translate(tmpV1.set(camera.direction).scl(deltaY * config.translateUnits))
            }
            if (Gdx.input.isKeyPressed(keybinds.home)) {
                moveTarget = Vector3(config.homeTarget)
                prevTarget.set(target)
                prevPosition.set(camera.position)
                movePosition = Vector3().set(config.homeTarget).sub(prevPosition).nor().scl(-config.distanceToHome).add(config.homeTarget)
                time = 0f
            } else {
                moveTarget = null
            }
            camera.up.set(Vector3.Y)
            camera.lookAt(target)
            changed = true

        }

        fun lerpRotateAround(target: Vector3, axis: Vector3, angle: Float, lerp: Float) {
            var tmpV1 = Vector3()
            var tmpV2 = Vector3()
            tmpV2.set(camera.position)
            tmpV1.set(target)
            tmpV1.sub(camera.position)
            tmpV2.add(tmpV1)
            camera.rotate(axis, angle)
            tmpV1.rotate(axis, angle)
            tmpV2.add(-tmpV1.x, -tmpV1.y, -tmpV1.z)

            camera.position.slerp(tmpV2, Gdx.graphics.deltaTime*20*lerp)
        }












        override fun keyDown(keycode: Int): Boolean {

            val rotationSteps = 90f / 6
            if (keycode == (Input.Keys.A)) {
                camera.rotateAround(target, Vector3.Y, -rotationSteps)
                changed = true
            }
            if (keycode == (Input.Keys.D)) {
                camera.rotateAround(target, Vector3.Y, rotationSteps)
                changed = true
            }
            if (keycode == (Input.Keys.W)) {
                camera.rotateAround(target, tangent, -rotationSteps)
                changed = true
            }
            if (keycode == (Input.Keys.S)) {
                camera.rotateAround(target, tangent, rotationSteps)
                changed = true
            }
            if (keycode == (Input.Keys.CONTROL_LEFT) || keycode == (Input.Keys.CONTROL_RIGHT)) {

                keybinds.zoom = keybinds.button
            } else
            if (keycode == (Input.Keys.SHIFT_LEFT) || keycode == (Input.Keys.SHIFT_RIGHT)) {
                keybinds.pan = keybinds.button

            } else
            {

                keybinds.rotate = keybinds.button

            }

            return super.keyDown(keycode)
        }



        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            keybinds.button=button
            startX = screenX.toFloat()
            startY = screenY.toFloat()
            oldCameraDir.set(camera.direction)
            oldCameraPos.set(camera.position)

            return super.touchDown(screenX, screenY, pointer, button)
        }

        override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {


            return touchDown(x, y, pointer, button)

        }


        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            keybinds.button=button
            return super.touchUp(screenX, screenY, pointer, button)
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val result = super.touchDragged(screenX, screenY, pointer)
            if (result || this.keybinds.button < 0) return result
            val deltaX = (screenX - startX) / Gdx.graphics.width
            val deltaY = (startY - screenY) / Gdx.graphics.height
            startX = screenX.toFloat()
            startY = screenY.toFloat()
            process(keybinds.button, deltaX, deltaY)
            return true
        }



        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            return zoom(amountY * scrollFactor*translateUnits)
        }

        override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun longPress(x: Float, y: Float): Boolean {
            TODO("Not yet implemented")
        }

        override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
            TODO("Not yet implemented")
        }

        override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
            TODO("Not yet implemented")
        }

        protected fun pinchZoom(amount: Float): Boolean {
            return zoom(pinchZoomFactor * amount)
        }

        override fun zoom(initialDistance: Float, distance: Float): Boolean {
            val newZoom = distance - initialDistance
            val amount: Float = newZoom - previousZoom
            previousZoom = newZoom
            val w = Gdx.graphics.width.toFloat()
            val h = Gdx.graphics.height.toFloat()
            return pinchZoom(amount / if (w > h) h else w)
        }

        fun zoom(amount: Float): Boolean {
            tmpV1.set(camera.direction).scl(amount)
//            lerp from current position to new position
            camera.position.slerp(tmpV1.add(camera.position), Gdx.graphics.deltaTime*20*lerpAmount)
            return true
        }

        override fun pinch(
            initialPointer1: Vector2?,
            initialPointer2: Vector2?,
            pointer1: Vector2?,
            pointer2: Vector2?
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun pinchStop() {
            TODO("Not yet implemented")
        }


    }

    }

class Raycaster {

    data class RaycastResult(var hit: Boolean = false, var pos: Vector3 = Vector3(), var normal: Vector3 = Vector3())

    interface Raycast {
        fun castRay(ray: Ray,pos:Vector3,callback: RaycastCallback) : RaycastResult
    }

    interface SupplyRay {
        fun supplyRay(x: Float,y:Float): Ray
    }

    interface RaycastCallback {
        fun onRaycast(result: RaycastResult)
    }

}

