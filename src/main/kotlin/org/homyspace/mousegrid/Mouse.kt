/*
 * It's responsible for defining mouse and commands
 */
package org.homyspace.mousegrid

enum class Command(val literal: String) {
    F("FORWARD"),
    B("BACKWARD"),
    L("LEFT"),
    R("RIGHT")
}

class Mouse(
        private val grid: Grid = Grid(),
        private val position: PositivePoint = PositivePoint(),
        private val direction: Direction = Direction.N) {

    init {
        val isMouseInsideGrid = grid.containsPoint(position)
        require(isMouseInsideGrid) { "mouse 'position' should be inside map" }
        val isMouseInsideObstacle = grid.isObstacleOn(position).isDefined()
        require(!isMouseInsideObstacle) { "mouse 'position' should NOT be inside an obstacle" }
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
            Command.F -> move(direction.unitMovement())
            Command.B -> move(direction.unitMovement().invertWay())
            Command.L -> turn(direction.nextCounterClockwise())
            Command.R -> turn(direction.nextClockwise())
        }
    }

    private fun move(movement: Movement) : Mouse {
        return grid.move(position, movement)
                .fold({ this }, { newPosition -> Mouse(grid, newPosition, direction) })
    }

    private fun turn(newDirection: Direction) = Mouse(grid, position, newDirection)

}


