package org.homyspace.mousegrid

import spock.lang.Specification

class MapSpec extends Specification {

    def 'can be created width valid dimensions'() {
        when:
        Map map = new Map(5, 10)

        then:
        map.area.width == 5
        map.area.height == 10
    }

    def 'complains when created with invalid dimensions'() {
        when:
        new Map(-5, 10)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "'width' should be greater or equal than 0"
    }

    def 'point is inside map'() {
        when:
        Map map = new Map(2, 2)
        Point point = new Point(0, 0)

        then:
        map.isInside(point) == true
    }

    def 'point is outside map'() {
        when:
        def map = new Map(2, 2)
        def point = new Point(3, 0)

        then:
        map.isInside(point) == false
    }

    def 'should return its size'() {
        given:
        Map map = new Map(5, 10)

        when:
        Area mapArea = map.area

        then:
        mapArea == new Area(5, 10)
    }

    def 'can provide obstacles inside map'() {
        given:
        Map map = new Map(10, 10, new Obstacle(0, 0), new Obstacle(2, 3))

        when:
        Set<Obstacle> obstacles = map.obstacles

        then:
        obstacles.size() == 2
    }

    def 'obstacles outside map are ignored'() {
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
        map.move(new Point(0, 0), new Movement(10, 0))

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'New position is outside map'
    }

}
