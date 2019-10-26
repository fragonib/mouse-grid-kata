/*
 * It's responsible for defining mouse operations
 */
package org.homyspace.mousegrid

open class Mouse(
        private val grid: Grid = Grid(),
        private val position: PositivePoint = PositivePoint(),
        private val direction: Direction = Direction.N) {

    init {
        require(grid.containsPoint(position)) {
            "mouse 'position' should be inside grid"
        }
        require(grid.obstacleOn(position).isEmpty()) {
            "mouse 'position' should NOT be inside an obstacle"
        }
    }
    
    fun broadcastPosition() : PositivePoint {
        return position
    }

    fun broadcastDirection() : Direction {
        return direction
    }

    fun receiveCommands(commands: String) : List<Command> {
        return commands.toCharArray()
                .map { readCommand(it) }
    }

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command ->
                    if (mouse is BlockedMouse) mouse else mouse.doCommand(command)
                })
    }

    private fun readCommand(commandChar: Char) : Command {
        return Command.valueOf(commandChar.toString())
    }

    private fun doCommand(command: Command): Mouse {
        
        return when (val mouseAction = command.mouseAction) {

            is MouseAction.MovementAction -> {
                mouseAction.move(grid, position, direction)
                        .fold({ 
                            BlockedMouse(grid, position, direction, it)
                        }, { 
                            Mouse(grid, it, direction)
                        })
            }

            is MouseAction.TurningAction -> {
                val newDirection = mouseAction.turn(direction)
                Mouse(grid, position, newDirection)
            }

        }
    }

}

class BlockedMouse(
        grid: Grid, position: PositivePoint, direction: Direction,
        private val blockingObstacle: Obstacle) : Mouse(grid, position, direction) {

    fun broadcastObstacle() : Obstacle {
        return blockingObstacle
    }

} 


