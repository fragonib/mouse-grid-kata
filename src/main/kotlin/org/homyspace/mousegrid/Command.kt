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

interface ClockOrdered<T> {
    fun nextClockwise() : T
    fun nextCounterClockwise() : T
}

enum class Direction(val literal: String) : ClockOrdered<Direction> {

    N("NORTH") {
        override fun nextClockwise(): Direction = E
        override fun nextCounterClockwise(): Direction = W
        override fun unitMovement(): Movement = Movement(0, 1)
    },

    E("EAST")  {
        override fun nextClockwise(): Direction = S
        override fun nextCounterClockwise(): Direction = N
        override fun unitMovement(): Movement = Movement(1, 0)
    },

    S("SOUTH") {
        override fun nextClockwise(): Direction = W
        override fun nextCounterClockwise(): Direction = E
        override fun unitMovement(): Movement = Movement(0, -1)
    },

    W("WEST") {
        override fun nextClockwise(): Direction = N
        override fun nextCounterClockwise(): Direction = S
        override fun unitMovement(): Movement = Movement(-1, 0)
    };

    abstract fun unitMovement(): Movement

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
        return grid.move(currentPosition, currentDirection.unitMovement())
    }
}

object MoveBackwards : MouseAction.MovementAction() {
    override fun move(grid: Grid, currentPosition: PositivePoint, currentDirection: Direction):
            Either<Obstacle, PositivePoint> {
        return grid.move(currentPosition, currentDirection.unitMovement().invertWay())
    }
}
