package org.homyspace.mousegrid

import spock.lang.Specification

class AreaSpec extends Specification {

    def 'can be created width valid dimensions'() {
        when:
        Area area = new Area(5, 10)

        then:
        area.getWidth() == 5
        area.getHeight() == 10
    }

    def 'complains with no valid dimensions'() {
        when:
        new Area(-5, 10)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "'width' should be greater or equal than 0"
    }

    def 'point is inside area'() {
        when:
        Area area = new Area(2, 2)
        Point point = new Point(0, 0)

        then:
        area.isInside(point) == true
    }

    def 'point is outside area'() {
        when:
        def area = new Area(2, 2)
        def point = new Point(3, 0)

        then:
        area.isInside(point) == false
    }

}
