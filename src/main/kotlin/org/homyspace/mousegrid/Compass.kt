/*
 * It's responsible for orienting
 */
package org.homyspace.mousegrid

interface ClockOrdered<T> {
    fun nextClockwise() : T
    fun nextCounterClockwise() : T
}

interface Pointing {
    fun unitVector(): Vector
}

enum class Direction(val literal: String) : ClockOrdered<Direction>, Pointing {

    N("NORTH") {
        override fun nextClockwise(): Direction = E
        override fun nextCounterClockwise(): Direction = W
        override fun unitVector(): Vector = Vector(0, 1)
    },

    E("EAST")  {
        override fun nextClockwise(): Direction = S
        override fun nextCounterClockwise(): Direction = N
        override fun unitVector(): Vector = Vector(1, 0)
    },

    S("SOUTH") {
        override fun nextClockwise(): Direction = W
        override fun nextCounterClockwise(): Direction = E
        override fun unitVector(): Vector = Vector(0, -1)
    },

    W("WEST") {
        override fun nextClockwise(): Direction = N
        override fun nextCounterClockwise(): Direction = S
        override fun unitVector(): Vector = Vector(-1, 0)
    };

}