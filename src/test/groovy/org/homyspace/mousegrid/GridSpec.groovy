package org.homyspace.mousegrid

import spock.lang.Specification

class GridSpec extends Specification {

    def 'has a default size of (10, 10)'() {
        when:
        Grid grid = new Grid()

        then:
        grid.area == new Area(10, 10)
    }

    def 'can provide its size'() {
        given:
        Grid grid = new Grid(5, 10)

        when:
        Area gridArea = grid.area

        then:
        gridArea == new Area(5, 10)
    }

    def 'can provide obstacles inside grid'() {
        given:
        def firstObstacle = new Obstacle(0, 0)
        def secondObstacle = new Obstacle(2, 3)
        Grid grid = new Grid(10, 10, firstObstacle, secondObstacle)

        when:
        Set<Obstacle> obstacles = grid.obstacles

        then:
        obstacles.size() == 2
    }

    def 'obstacles outside grid are ignored'() {
        given:
        def insideObstacle = new Obstacle(4, 4)
        def outsideObstacle = new Obstacle(10, 10)
        Grid grid = new Grid(5, 5, outsideObstacle, insideObstacle)

        when:
        Set<Obstacle> obstacles = grid.obstacles

        then:
        obstacles.size() == 1
        obstacles.contains(insideObstacle) == true
        obstacles.contains(outsideObstacle) == false
    }

    def 'detects no obstacles on specific points'() {
        given:
        def firstObstacle = new Obstacle(5, 5)
        def secondObstacle = new Obstacle(2, 3)
        Grid grid = new Grid(10, 10, firstObstacle, secondObstacle)

        when:
        def target = new PositivePoint(x, y)
        def optionalObstacle = grid.obstacleOn(target)

        then:
        optionalObstacle.isEmpty()

        where:
        x | y
        0 | 0
        2 | 2
        5 | 6
    }

    def 'detects obstacles on specific points'() {
        given:
        def firstObstacle = new Obstacle(5, 5)
        def secondObstacle = new Obstacle(2, 3)
        Grid grid = new Grid(10, 10, firstObstacle, secondObstacle)

        when:
        def target = new PositivePoint(x, y)
        def optionalObstacle = grid.obstacleOn(target)

        then:
        optionalObstacle.isDefined()

        where:
        x | y
        5 | 5
        2 | 3
    }

    def 'does movements within the grid'() {
        given:
        Grid grid = new Grid(10, 10)

        when:
        def from = new PositivePoint(fromX, fromY)
        def movement = new Vector(shiftX, shiftX)
        def newPosition = grid.move(from, movement).toOption().orNull()

        then:
        newPosition == new PositivePoint(expectedX, expectedY)

        where:
        fromX | fromY | shiftX | shiftY | expectedX | expectedY
        0     | 0     | 0      | 0      | 0         | 0
        0     | 0     | 5      | 5      | 5         | 5
        0     | 0     | 9      | 9      | 9         | 9
    }

    def 'wraps movements across grid edges'() {
        given:
        Grid grid = new Grid(10, 10)

        when:
        def from = new PositivePoint(fromX, fromY)
        def movement = new Vector(shiftX, shiftX)
        def newPosition = grid.move(from, movement).toOption().orNull()

        then:
        newPosition == new PositivePoint(expectedX, expectedY)

        where:
        fromX | fromY | shiftX | shiftY | expectedX | expectedY
        0     | 0     | 10     | 10     | 0         | 0
        0     | 0     | -1     | -1     | 9         | 9
        0     | 0     | 25     | 25     | 5         | 5
    }

}
