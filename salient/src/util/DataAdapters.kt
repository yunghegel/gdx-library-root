package util

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlinx.serialization.Serializable

@Serializable
data class Vector2Data(val x: Float, val y: Float) {
    companion object {
        val zero = Vector2Data(0f, 0f)
    }
}
@Serializable
data class Vector3Data(val x: Float, val y: Float, val z: Float) {
    companion object {
        val zero = Vector3Data(0f, 0f, 0f)
    }
}
@Serializable
data class Matrix4Data(val m00: Float, val m01: Float, val m02: Float, val m03: Float,
                       val m10: Float, val m11: Float, val m12: Float, val m13: Float,
                       val m20: Float, val m21: Float, val m22: Float, val m23: Float,
                       val m30: Float, val m31: Float, val m32: Float, val m33: Float) {
    companion object {
        val identity = Matrix4Data(1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f)
        fun fromMat4(matrix: Matrix4) : Matrix4Data {
           return Matrix4Data(matrix.`val`[0], matrix.`val`[1], matrix.`val`[2], matrix.`val`[3],
                    matrix.`val`[4], matrix.`val`[5], matrix.`val`[6], matrix.`val`[7],
                    matrix.`val`[8], matrix.`val`[9], matrix.`val`[10], matrix.`val`[11],
                    matrix.`val`[12], matrix.`val`[13], matrix.`val`[14], matrix.`val`[15])
        }
        fun toMat4(matrix: Matrix4Data): Matrix4 {
            return Matrix4(floatArrayOf(matrix.m00, matrix.m01, matrix.m02, matrix.m03,
                    matrix.m10, matrix.m11, matrix.m12, matrix.m13,
                    matrix.m20, matrix.m21, matrix.m22, matrix.m23,
                    matrix.m30, matrix.m31, matrix.m32, matrix.m33))
        }
    }

}
@Serializable
data class ColorData(val r: Float, val g: Float, val b: Float, val a: Float) {
    companion object {
        val white = ColorData(1f, 1f, 1f, 1f)
        val black = ColorData(0f, 0f, 0f, 1f)
        val red = ColorData(1f, 0f, 0f, 1f)
        val green = ColorData(0f, 1f, 0f, 1f)
        val blue = ColorData(0f, 0f, 1f, 1f)
        val yellow = ColorData(1f, 1f, 0f, 1f)
        val cyan = ColorData(0f, 1f, 1f, 1f)
        val magenta = ColorData(1f, 0f, 1f, 1f)
    }
}
