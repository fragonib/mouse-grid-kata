/*
 * It's responsible for defining mouse commands
 */
package org.homyspace.mousegrid

import arrow.core.Either

enum class Command(val mouseAction: MouseAction) {
    F (MoveForward),
    B (MoveBackwards),
    L (TurnLeft),
    R (TurnRight)
}

sealed class MouseAction {
    abstract class TurningAction : MouseAction() {
        abstract fun turn(currentDirection: Direction): Direction
    }
    abstract class MovementAction : MouseAction() {
        abstract fun move(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction):
                Either<Obstacle, PositivePoint>
    }
}

object TurnLeft : MouseAction.TurningAction() {
    override fun turn(currentDirection: Direction): Direction {
        return currentDirection.nextCounterClockwise()
    }
}

object TurnRight : MouseAction.TurningAction() {
    override fun turn(currentDirection: Direction): Direction {
        return currentDirection.nextClockwise()
    }
}

object MoveForward : MouseAction.MovementAction() {
    override fun move(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction):
            Either<Obstacle, PositivePoint> {
        return grid.move(currentPosition, currentDirection.unitVector())
    }
}

object MoveBackwards : MouseAction.MovementAction() {
    override fun move(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction):
            Either<Obstacle, PositivePoint> {
        return grid.move(currentPosition, currentDirection.unitVector().invertWay())
    }
}

