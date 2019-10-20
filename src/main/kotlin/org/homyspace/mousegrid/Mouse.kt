/*
 * It's responsible for defining mouse operations
 */
package org.homyspace.mousegrid

class Mouse(
        private val grid: Grid = Grid(),
        private val position: PositivePoint = PositivePoint(),
        private val direction: Direction = Direction.N) {

    init {
        require(grid.containsPoint(position)) {
            "mouse 'position' should be inside map"
        }
        require(grid.isObstacleOn(position).isEmpty()) {
            "mouse 'position' should NOT be inside an obstacle"
        }
    }

    fun broadcastPosition() : PositivePoint {
        return position
    }

    fun broadcastDirection() : Direction {
        return direction
    }

    fun receiveCommands(commands: String) : List<MouseAction> {
        return commands.toCharArray()
                .map { readCommand(it) }
    }

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command -> mouse.doCommand(command)} )
    }

    private fun readCommand(commandChar: Char) : MouseAction {
        return Command.valueOf(commandChar.toString()).mouseAction
    }

    private fun doCommand(command: MouseAction): Mouse {
        return when (command) {
            is MouseAction.MovementAction -> {
                val newPosition = command.move(grid, position, direction)
                        .mapLeft { obstacle -> position }
                        .fold({ position }, { it })
                Mouse(grid, newPosition, direction)
            }
            is MouseAction.TurningAction -> {
                val newDirection = command.turn(direction)
                Mouse(grid, position, newDirection)
            }
        }
    }

}


