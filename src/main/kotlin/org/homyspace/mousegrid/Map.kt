package org.homyspace.mousegrid

import java.lang.IllegalArgumentException
import java.util.*

data class PositivePoint(val x: Int = 0, val y: Int = 0) {
    init {
        require(x >= 0) { "'x' should be greater or equal than 0" }
        require(y >= 0) { "'y' should be greater or equal than 0" }
    }
}

data class Area(val width: Int, val height: Int) {

    init {
        require(width > 0) { "'width' should be greater or equal than 0" }
        require(height > 0) { "'height' should be greater or equal than 0" }
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

    private val area: Area = Area(width, height)
    private var obstacles: Set<Obstacle> = Collections.emptySet()

    constructor(width: Int = 10, height: Int = 10, vararg obstacles: Obstacle) :
            this(width, height) {
        this.obstacles = obstacles
                .filter { area.isInside(it.position) }
                .toSet()
    }

    fun move(from: PositivePoint, movement: Movement) : PositivePoint {
        val newPosition = PositivePoint(from.x + movement.xShift, from.y + movement.yShift)
        if (!isInside(newPosition))
            throw IllegalArgumentException("New position is outside map")
        return newPosition
    }

    fun isInside(point: PositivePoint): Boolean = area.isInside(point)

}