/*
 * It's responsible for defining mouse operations. It uses immutability and open/closed principle.
 */
package org.homyspace.mousegrid

abstract class Mouse(
        protected val grid: Grid,
        protected val position: PositivePoint,
        protected val direction: Direction) {

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

    abstract fun readCommand(commandChar: Char) : MouseAction

    abstract fun doCommand(mouseAction: MouseAction): Mouse

}

class BlockedMouse(
        grid: Grid = Grid(),
        position: PositivePoint = PositivePoint(),
        direction: Direction = Direction.N,
        private val blockingObstacle: Obstacle) : Mouse(grid, position, direction) {

    fun broadcastObstacle() : Obstacle {
        return blockingObstacle
    }

    override fun readCommand(commandChar: Char) : MouseAction {
        return Command.valueOf(commandChar.toString()).mouseAction
    }

    /**
     * A blocked mouse can't execute new commands
     */
    override fun doCommand(mouseAction: MouseAction): Mouse {
        return this
    }

}

class ReadyMouse(
        grid: Grid = Grid(),
        position: PositivePoint = PositivePoint(),
        direction: Direction = Direction.N) : Mouse(grid, position, direction) {

    override fun readCommand(commandChar: Char) : MouseAction {
        return Command.valueOf(commandChar.toString()).mouseAction
    }

    override fun doCommand(mouseAction: MouseAction) : Mouse {
        return mouseAction.execute(grid, position, direction)
    }

}


