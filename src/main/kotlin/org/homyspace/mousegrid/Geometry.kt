/*
 * It's responsible for defining space and geometry concepts
 */
package org.homyspace.mousegrid

data class PositivePoint(val x: Int = 0, val y: Int = 0) {
    init {
        require(x >= 0) { "'x=$x' and should be greater or equal than 0" }
        require(y >= 0) { "'y=$y' and should be greater or equal than 0" }
    }
}

data class Area(val width: Int, val height: Int) {

    init {
        require(width > 0) { "'width=$width' and should be greater than 0" }
        require(height > 0) { "'height=$height' and should be greater than 0" }
    }

    fun contains(point: PositivePoint): Boolean {
        return point.x <= this.width && point.y <= this.height
    }

}