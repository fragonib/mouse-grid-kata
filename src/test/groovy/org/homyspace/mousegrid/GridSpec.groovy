package org.homyspace.mousegrid

import spock.lang.Specification

class GridSpec extends Specification {

    def 'has a default size of (10, 10)'() {
        when:
        Grid map = new Grid()

        then:
        map.area == new Area(10, 10)
    }

    def 'can provide its size'() {
        given:
        Grid map = new Grid(5, 10)

        when:
        Area mapArea = map.area

        then:
        mapArea == new Area(5, 10)
    }

    def 'can provide obstacles inside'() {
        given:
        Grid map = new Grid(10, 10, new Obstacle(0, 0), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 2
    }

    def 'obstacles outside are ignored'() {
        given:
        Grid map = new Grid(5, 10, new Obstacle(10, 10), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 1
    }

    def 'moves within the map'() {
        given:
        Grid map = new Grid(10, 10)

        when:
        def from = new PositivePoint(fromX, fromY)
        def movement = new Movement(shiftX, shiftX)
        def newPosition = map.move(from, movement)

        then:
        newPosition == new PositivePoint(expectedX, expectedY)

        where:
        fromX | fromY | shiftX | shiftY | expectedX | expectedY
        0     | 0     | 0      | 0      | 0         | 0
        0     | 0     | 5      | 5      | 5         | 5
        0     | 0     | 9      | 9      | 9         | 9
    }

    def 'wraps movement across edges'() {
        given:
        Grid map = new Grid(10, 10)

        when:
        def from = new PositivePoint(fromX, fromY)
        def movement = new Movement(shiftX, shiftX)
        def newPosition = map.move(from, movement)

        then:
        newPosition == new PositivePoint(expectedX, expectedY)

        where:
        fromX | fromY | shiftX | shiftY | expectedX | expectedY
        0     | 0     | 10     | 10     | 0         | 0
        0     | 0     | -1     | -1     | 9         | 9
        0     | 0     | 25     | 25     | 5         | 5
    }

}
