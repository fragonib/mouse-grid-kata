package org.homyspace.mousegrid;

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

class Mouse(initialPosition: Point, initialDirection: Direction) {

    private var currentPosition: Point = initialPosition
    private var currentDirection: Direction = initialDirection

    fun broadcastPosition() : Point {
        return currentPosition
    }

    fun broadcastDirection() : Direction {
        return currentDirection
    }

}

