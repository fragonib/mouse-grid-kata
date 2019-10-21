/*
 * It's responsible for defining mouse operations
 */
package org.homyspace.mousegrid

open class Mouse(
        private val grid: Grid = Grid(),
        private val currentPosition: PositivePoint = PositivePoint(),
        private val currentDirection: Direction = Direction.N) {

    init {
        require(grid.containsPoint(currentPosition)) {
            "mouse 'position' should be inside map"
        }
        require(grid.isObstacleOn(currentPosition).isEmpty()) {
            "mouse 'position' should NOT be inside an obstacle"
        }
    }
    
    fun broadcastPosition() : PositivePoint {
        return currentPosition
    }

    fun broadcastDirection() : Direction {
        return currentDirection
    }

    fun receiveCommands(commands: String) : List<Command> {
        return commands.toCharArray()
                .map { readCommand(it) }
    }

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command -> mouse.doCommand(command)} )
    }

    private fun readCommand(commandChar: Char) : Command {
        return Command.valueOf(commandChar.toString())
    }

    private fun doCommand(command: Command): Mouse {
        
        return when (val mouseAction = command.mouseAction) {

            is MouseAction.MovementAction -> {
                mouseAction.move(grid, currentPosition, currentDirection)
                        .fold({ 
                            BlockedMouse(grid, currentPosition, currentDirection, it) 
                        }, { 
                            Mouse(grid, it, currentDirection) 
                        })
            }

            is MouseAction.TurningAction -> {
                val newDirection = mouseAction.turn(currentDirection)
                Mouse(grid, currentPosition, newDirection)
            }

        }
    }

}

class BlockedMouse(
        grid: Grid, currentPosition: PositivePoint, currentDirection: Direction,
        private val blockingObstacle: Obstacle) : Mouse(grid, currentPosition, currentDirection) {

    fun broadcastObstacle() : Obstacle {
        return blockingObstacle
    }

} 


