package org.homyspace.mousegrid;

enum class Command(literal: String) {
    F("FORWARD"),
    B("BACKWARD")
}

class Mouse() {

    private var currentPosition: Point = Point(0, 0)
    private var currentDirection: Direction = Direction.NORTH

    constructor(initialPosition: Point, initialDirection: Direction) : this() {
        this.currentPosition = initialPosition
        this.currentDirection = initialDirection
    }

    fun broadcastPosition() : Point {
        return currentPosition
    }

    fun broadcastDirection() : Direction {
        return currentDirection
    }

    fun receiveCommands(commands: String): List<Command> {
        return commands.toCharArray()
                .map { Command.valueOf(it.toString()) }
                .toList()
    }

}

