package org.homyspace.mousegrid

import java.lang.IllegalArgumentException
import java.util.*

class Obstacle(x: Int, y: Int) {
    val position: Point = Point(x, y)
}

class Step(val x: Int, val y: Int) {
    fun invert() : Step {
        return Step(x * -1 , y * -1)
    }
}

class Map(private val area: Area = Area(10, 10)) {

    private var obstacles: Set<Obstacle>

    init {
        this.obstacles = Collections.emptySet()
    }

    constructor(area: Area, vararg obstacles: Obstacle) : this(area) {
        this.obstacles = obstacles
                .filter { area.isInside(it.position) }
                .toSet()
    }

    fun size() : Area {
        return this.area
    }

    fun move(from: Point, step: Step) : Point {
        val newPosition = Point(from.x + step.x, from.y + step.y)
        if (!isInside(newPosition))
            throw IllegalArgumentException("New position is outside map")
        return newPosition
    }

    fun isInside(point: Point): Boolean = area.isInside(point)

}