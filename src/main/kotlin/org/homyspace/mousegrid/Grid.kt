/*
 * It's responsible for defining grid space where movements are possible
 */
package org.homyspace.mousegrid

import arrow.core.Either
import arrow.core.Option
import java.util.*

class Obstacle(x: Int, y: Int) {
    private val position: PositivePoint = PositivePoint(x, y)

    fun isOccupying(targetPoint: PositivePoint) = targetPoint == position

    fun isInside(area: Area) = area.contains(position)
}

class Grid(width: Int = 10, height: Int = 10) {

    val area: Area = Area(width, height)
    var obstacles: Set<Obstacle> = Collections.emptySet()

    constructor(width: Int = 10, height: Int = 10, vararg obstacles: Obstacle) :
            this(width, height) {
        this.obstacles = obstacles
                .filter { it.isInside(area) }
                .toSet()
    }

    fun containsPoint(targetPoint: PositivePoint) = area.contains(targetPoint)

    fun isObstacleOn(targetPoint: PositivePoint) : Option<Obstacle> {
        return Option.fromNullable(obstacles.find { it.isOccupying(targetPoint) })
    }

    fun move(from: PositivePoint, movement: Vector) : Either<Obstacle, PositivePoint> {
        val targetPoint = calculateNewPoint(from, movement)
        return isObstacleOn(targetPoint)
                .toEither { targetPoint }
                .swap()
    }

    private fun calculateNewPoint(from: PositivePoint, movement: Vector): PositivePoint {
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