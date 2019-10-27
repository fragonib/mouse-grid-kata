/*
 * It's responsible for defining mouse operations. It uses immutability and open/closed principle.
 */
package org.homyspace.mousegrid

abstract class Mouse(
        protected val grid: Grid,
        protected val position: PositivePoint,
        protected val direction: Direction) : CommandReader, CommandExecutor {

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

    fun receiveCommands(commands: String) : List<MouseAction> {
        return commands.toCharArray()
                .map { readCommand(it) }
    }

    fun executeCommands(commands: String) : Mouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command -> mouse.doCommand(command) })
    }

}

/**
 * A mouse that is able to execute new commands
 */
class ReadyMouse(
        grid: Grid = Grid(),
        position: PositivePoint = PositivePoint(),
        direction: Direction = Direction.N) : Mouse(grid, position, direction) {

    override fun doCommand(mouseAction: MouseAction) : Mouse {
        return mouseAction.execute(grid, position, direction)
    }

}

/**
 * A blocked mouse can't execute new commands and reports the obstacle is blocking it
 */
class BlockedMouse(
        grid: Grid = Grid(),
        position: PositivePoint = PositivePoint(),
        direction: Direction = Direction.N,
        private val blockingObstacle: Obstacle) : Mouse(grid, position, direction) {

    fun broadcastObstacle() : Obstacle {
        return blockingObstacle
    }

    override fun doCommand(mouseAction: MouseAction): Mouse {
        return this
    }

}


