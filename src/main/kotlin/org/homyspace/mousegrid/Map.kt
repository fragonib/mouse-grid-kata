package org.homyspace.mousegrid

import java.util.*

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

    fun isInside(point: PositivePoint): Boolean {
        return point.x <= this.width && point.y <= this.height
    }

}

class Movement(val xShift: Int, val yShift: Int) {

    fun invert() : Movement {
        return Movement(xShift * -1 , yShift * -1)
    }
}

class Obstacle(x: Int, y: Int) {
    val position: PositivePoint = PositivePoint(x, y)
}

class Map(width: Int = 10, height: Int = 10) {

    val area: Area = Area(width, height)
    var obstacles: Set<Obstacle> = Collections.emptySet()

    constructor(width: Int = 10, height: Int = 10, vararg obstacles: Obstacle) :
            this(width, height) {
        this.obstacles = obstacles
                .filter { area.isInside(it.position) }
                .toSet()
    }

    fun move(from: PositivePoint, movement: Movement) : PositivePoint {
        val newX = wrap(from.x + movement.xShift, area.width)
        val newY = wrap(from.y + movement.yShift, area.height)
        return PositivePoint(newX, newY)
    }

    private fun wrap(value: Int, max: Int): Int {
        val wrappedValue = value % max
        return if (wrappedValue < 0) wrappedValue + max else wrappedValue
    }

}