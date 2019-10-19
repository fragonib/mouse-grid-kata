package org.homyspace.mousegrid

data class Point(val x: Int = 0, val y: Int = 0)

data class Area(val width: Int, val height: Int) {

    init {
        require(width > 0) { "'width' should be greater or equal than 0" }
        require(height > 0) { "'height' should be greater or equal than 0" }
    }

    fun isInside(point: Point): Boolean {
        return point.x < this.width && point.y < this.height
    }

}
