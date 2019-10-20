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
        val isMouseInsideGrid = grid.area.isInside(position)
        require(isMouseInsideGrid) { "mouse 'position' should be inside map" }
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
            Command.F -> Mouse(grid, grid.move(position, direction.unitMovement()), direction)
            Command.B -> Mouse(grid, grid.move(position, direction.unitMovement().invert()), direction)
            Command.L -> Mouse(grid, position, direction.nextCounterClockwise())
            Command.R -> Mouse(grid, position, direction.nextClockwise())
        }
    }

}

