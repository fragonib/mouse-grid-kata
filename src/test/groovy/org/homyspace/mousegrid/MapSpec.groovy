package org.homyspace.mousegrid

import spock.lang.Specification

class MapSpec extends Specification {

    def 'should return its size'() {
        given:
        Map map = new Map(new Area(5, 10))

        when:
        Area mapSize = map.size()

        then:
        mapSize == new Area(5, 10)
    }

    def 'can provide obstacles inside map'() {
        given:
        Map map = new Map(new Area(5, 10), new Obstacle(0, 0), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 2
    }

    def 'obstacles outside map are ignored'() {
        given:
        Map map = new Map(new Area(5, 10), new Obstacle(10, 10), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 1
    }

    def 'complains if movement is outside map'() {
        given:
        Map map = new Map(new Area(5, 5))

        when:
        map.move(new Point(0, 0), new Step(10, 0))

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'New position is outside map'
    }

}
