package org.homyspace.mousegrid

enum class Command(val literal: String) {
    F("FORWARD"),
    B("BACKWARD"),
    L("LEFT"),
    R("RIGHT")
}

enum class Direction(val literal: String, val step: Step) {
    N("NORTH", Step(0, 1)),
    S("SOUTH", Step(0, -1)),
    W("WEST",  Step(-1, 0)),
    E("EAST",  Step(1, 0)),
}

class Mouse(
        private val map: Map = Map(),
        private val position: Point = Point(),
        private val direction: Direction = Direction.N) {

    init {
        require(map.isInside(position)) { "mouse 'position' should be inside map" }
    }

    fun broadcastPosition() : Point {
        return position
    }

    fun broadcastDirection() : Direction {
        return direction
    }

    fun receiveCommands(commands: String) : List<Command> {
        return commands.toCharArray().map { readCommand(it) }
    }

    private fun readCommand(it: Char) = Command.valueOf(it.toString())

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command -> mouse.doCommand(command)} )
    }

    private fun doCommand(command: Command): Mouse {
        return when (command) {
            Command.F -> Mouse(map, map.move(position, direction.step), direction)
            Command.B -> Mouse(map, map.move(position, direction.step.invert()), direction)
            Command.L -> Mouse(map, position, turnLeft())
            Command.R -> Mouse(map, position, turnRight())
        }
    }

    private fun turnLeft() : Direction {
        return when(direction) {
            Direction.N -> Direction.W
            Direction.S -> Direction.E
            Direction.W -> Direction.S
            Direction.E -> Direction.N
        }
    }

    private fun turnRight() : Direction {
        return when(direction) {
            Direction.N -> Direction.E
            Direction.S -> Direction.W
            Direction.W -> Direction.N
            Direction.E -> Direction.S
        }
    }


}

