/*
 * It's responsible for defining grid space where movements are possible
 */
package org.homyspace.mousegrid

import arrow.core.Either
import arrow.core.Option

class Obstacle(x: Int, y: Int) {
    private val position: PositivePoint = PositivePoint(x, y)

    fun occupies(targetPoint: PositivePoint) = targetPoint == position

    fun isInside(area: Area) = area.contains(position)
}

class Grid(width: Int = 10, height: Int = 10, vararg obstacles: Obstacle = arrayOf()) {

    val area: Area = Area(width, height)
    val obstacles: Set<Obstacle>

    init {
        this.obstacles = obstacles
                .filter { it.isInside(area) }
                .toSet()
    }

    fun containsPoint(targetPoint: PositivePoint) = area.contains(targetPoint)

    fun obstacleOn(targetPoint: PositivePoint) : Option<Obstacle> {
        return Option.fromNullable(obstacles.find { it.occupies(targetPoint) })
    }

    fun move(from: PositivePoint, movement: Vector) : Either<Obstacle, PositivePoint> {
        val targetPoint = calculateNewPoint(from, movement)
        return obstacleOn(targetPoint)
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