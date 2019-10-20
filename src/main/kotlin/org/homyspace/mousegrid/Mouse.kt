package org.homyspace.mousegrid

enum class Command(val literal: String) {
    F("FORWARD"),
    B("BACKWARD"),
    L("LEFT"),
    R("RIGHT")
}

interface ClockOrdered {
    fun clockwise() : Direction
    fun antiClockwise() : Direction
}

enum class Direction(val literal: String) : ClockOrdered {

    N("NORTH") {
        override fun clockwise(): Direction = E
        override fun antiClockwise(): Direction = W
        override fun unitMovement(): Movement = Movement(0, 1)
    },

    E("EAST")  {
        override fun clockwise(): Direction = S
        override fun antiClockwise(): Direction = N
        override fun unitMovement(): Movement = Movement(1, 0)
    },

    S("SOUTH") {
        override fun clockwise(): Direction = W
        override fun antiClockwise(): Direction = E
        override fun unitMovement(): Movement = Movement(0, -1)
    },

    W("WEST") {
        override fun clockwise(): Direction = N
        override fun antiClockwise(): Direction = S
        override fun unitMovement(): Movement = Movement(-1, 0)
    };

    abstract fun unitMovement(): Movement

}

class Mouse(
        private val map: Map = Map(),
        private val position: PositivePoint = PositivePoint(),
        private val direction: Direction = Direction.N) {

    init {
        require(map.area.isInside(position)) { "mouse 'position' should be inside map" }
    }

    fun broadcastPosition() : PositivePoint {
        return position
    }

    fun broadcastDirection() : Direction {
        return direction
    }

    fun receiveCommands(commands: String) : List<Command> {
        return commands.toCharArray().map { readCommand(it) }
    }

    private fun readCommand(commandChar: Char) = Command.valueOf(commandChar.toString())

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command -> mouse.doCommand(command)} )
    }

    private fun doCommand(command: Command): Mouse {
        return when (command) {
            Command.F -> Mouse(map, map.move(position, direction.unitMovement()), direction)
            Command.B -> Mouse(map, map.move(position, direction.unitMovement().invert()), direction)
            Command.L -> Mouse(map, position, direction.antiClockwise())
            Command.R -> Mouse(map, position, direction.clockwise())
        }
    }

}

