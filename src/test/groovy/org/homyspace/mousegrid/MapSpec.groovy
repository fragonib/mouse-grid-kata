package org.homyspace.mousegrid

import spock.lang.Specification

class MapSpec extends Specification {

    def 'has a default size of (10, 10)'() {
        when:
        Map map = new Map()

        then:
        map.area == new Area(10, 10)
    }

    def 'can provide its size'() {
        given:
        Map map = new Map(5, 10)

        when:
        Area mapArea = map.area

        then:
        mapArea == new Area(5, 10)
    }

    def 'can provide obstacles inside'() {
        given:
        Map map = new Map(10, 10, new Obstacle(0, 0), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 2
    }

    def 'obstacles outside are ignored'() {
        given:
        Map map = new Map(5, 10, new Obstacle(10, 10), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 1
    }

    def 'complains if movement is outside map'() {
        given:
        Map map = new Map(5, 5)

        when:
        map.move(new PositivePoint(0, 0), new Movement(10, 0))

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'New position is outside map'
    }

}
