/*
 * It's responsible for defining mouse commands an actions
 */
package org.homyspace.mousegrid

/**
 * Represents one-to-one relationship between a given Command and mouse Action
 */
enum class Command(val mouseAction: MouseAction) {
    F (MoveStepForward),
    B (MoveStepBackwards),
    L (SteppedTurnLeft),
    R (SteppedTurnRight)
}

/**
 * Represent an arbitrary action mouse can carry on
 */
interface MouseAction {
    fun execute(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction): Mouse
}

// --- Actions

object SteppedTurnLeft : MouseAction {
    override fun execute(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction): Mouse {
        return ReadyMouse(grid, currentPosition, currentDirection.nextCounterClockwise())
    }
}

object SteppedTurnRight : MouseAction {
    override fun execute(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction): Mouse {
        return ReadyMouse(grid, currentPosition, currentDirection.nextClockwise())
    }
}

object MoveStepForward : MouseAction {
    override fun execute(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction): Mouse {
        return grid.move(currentPosition, currentDirection.unitVector())
                .fold({
                    BlockedMouse(grid, currentPosition, currentDirection, it)
                }, {
                    ReadyMouse(grid, it, currentDirection)
                })
    }
}

object MoveStepBackwards : MouseAction {
    override fun execute(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction): Mouse {
        return grid.move(currentPosition, currentDirection.unitVector().invertWay())
                .fold({
                    BlockedMouse(grid, currentPosition, currentDirection, it)
                }, {
                    ReadyMouse(grid, it, currentDirection)
                })
    }
}

