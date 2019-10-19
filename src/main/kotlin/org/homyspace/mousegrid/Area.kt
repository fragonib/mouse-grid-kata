package org.homyspace.mousegrid

open class Point(val x: Int, val y: Int)

data class Area(val width: Int, val height: Int) {

    init {
        require(width > 0) { "'width' should be greater or equal than 0" }
        require(height > 0) { "'height' should be greater or equal than 0" }
    }

    fun isInside(point: Point): Boolean {
        return point.x < this.width && point.y < this.height
    }

}
