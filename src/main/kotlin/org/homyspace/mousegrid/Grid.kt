/*
 * It's responsible for defining grid space where movements are possible
 */
package org.homyspace.mousegrid

import java.util.*

interface ClockOrdered {
    fun nextClockwise() : Direction
    fun nextCounterClockwise() : Direction
}

enum class Direction(val literal: String) : ClockOrdered {

    N("NORTH") {
        override fun nextClockwise(): Direction = E
        override fun nextCounterClockwise(): Direction = W
        override fun unitMovement(): Movement = Movement(0, 1)
    },

    E("EAST")  {
        override fun nextClockwise(): Direction = S
        override fun nextCounterClockwise(): Direction = N
        override fun unitMovement(): Movement = Movement(1, 0)
    },

    S("SOUTH") {
        override fun nextClockwise(): Direction = W
        override fun nextCounterClockwise(): Direction = E
        override fun unitMovement(): Movement = Movement(0, -1)
    },

    W("WEST") {
        override fun nextClockwise(): Direction = N
        override fun nextCounterClockwise(): Direction = S
        override fun unitMovement(): Movement = Movement(-1, 0)
    };

    abstract fun unitMovement(): Movement

}

class Movement(val xShift: Int, val yShift: Int) {
    fun invert() : Movement {
        return Movement(xShift * -1 , yShift * -1)
    }
}

class Obstacle(x: Int, y: Int) {
    val position: PositivePoint = PositivePoint(x, y)
}

class Grid(width: Int = 10, height: Int = 10) {

    val area: Area = Area(width, height)
    var obstacles: Set<Obstacle> = Collections.emptySet()

    constructor(width: Int = 10, height: Int = 10, vararg obstacles: Obstacle) :
            this(width, height) {
        this.obstacles = obstacles
                .filter { area.isInside(it.position) }
                .toSet()
    }

    fun move(from: PositivePoint, movement: Movement) : PositivePoint {
        val newX = wrapEdge(from.x + movement.xShift, area.width)
        val newY = wrapEdge(from.y + movement.yShift, area.height)
        return PositivePoint(newX, newY)
    }

    private fun wrapEdge(coordinate: Int, limit: Int): Int {
        val wrappedCoord = coordinate % limit
        val positiveCoord = if (wrappedCoord < 0) wrappedCoord + limit else wrappedCoord
        return positiveCoord
    }

}