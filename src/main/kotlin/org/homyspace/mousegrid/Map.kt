package org.homyspace.mousegrid

import java.util.*

class Obstacle(x: Int, y: Int) : Point(x, y)

class Map(private val area: Area) {

    private var obstacles: Set<Obstacle>

    init {
        this.obstacles = Collections.emptySet()
    }

    constructor(area: Area, vararg obstacles: Obstacle) : this(area) {
        this.obstacles = obstacles.filter { area.isInside(it) }.toSet()
    }

    fun size() : Area {
        return this.area
    }

}