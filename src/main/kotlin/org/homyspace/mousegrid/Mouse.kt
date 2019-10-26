/*
 * It's responsible for defining mouse operations. It uses immutability and open/closed principle.
 */
package org.homyspace.mousegrid

abstract class BaseMouse(
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

    fun executeCommands(commands: String) : BaseMouse {
        return receiveCommands(commands)
                .fold(this, { mouse, command ->
                    if (mouse is BlockedMouse) mouse else mouse.doCommand(command)
                })
    }

    abstract fun readCommand(commandChar: Char) : MouseAction

    abstract fun doCommand(mouseAction: MouseAction): BaseMouse

}

class BlockedMouse(
        grid: Grid, position: PositivePoint, direction: Direction,
        private val blockingObstacle: Obstacle) : BaseMouse(grid, position, direction) {

    fun broadcastObstacle() : Obstacle {
        return blockingObstacle
    }

    override fun readCommand(commandChar: Char) : MouseAction {
        return Command.valueOf(commandChar.toString()).mouseAction
    }

    override fun doCommand(mouseAction: MouseAction): BaseMouse {
        return this
    }

}

class Mouse(
        grid: Grid = Grid(),
        position: PositivePoint = PositivePoint(),
        direction: Direction = Direction.N) : BaseMouse(grid, position, direction) {

    override fun readCommand(commandChar: Char) : MouseAction {
        return Command.valueOf(commandChar.toString()).mouseAction
    }

    override fun doCommand(mouseAction: MouseAction) : BaseMouse {

        when (mouseAction) {

            is MouseAction.MovementAction -> {
                val fold = mouseAction.move(grid, position, direction)
                        .fold({
                            BlockedMouse(grid, position, direction, it)
                        }, {
                            Mouse(grid, it, direction)
                        })
                return fold
            }

            is MouseAction.TurningAction -> {
                val newDirection = mouseAction.turn(direction)
                return Mouse(grid, position, newDirection)
            }

        }
    }

}


