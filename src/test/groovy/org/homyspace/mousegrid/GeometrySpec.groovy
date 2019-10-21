package org.homyspace.mousegrid

import spock.lang.Specification

class GeometrySpec extends Specification {

    def 'positive point has (0,0) coordinates as default'() {
        when:
        PositivePoint defaultPoint = new PositivePoint()

        then:
        defaultPoint == new PositivePoint(0, 0)
    }

    def 'positive point complains when created with no positive coordinates'() {
        when:
        new PositivePoint(x, y)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == expectedMessage

        where:
        x  | y   | expectedMessage
        -1 | 0   | "'x=-1' and should be greater or equal than 0"
        10 | -10 | "'y=-10' and should be greater or equal than 0"
    }

    def 'vector can be defined and inverted'() {
        when:
        def vector = new Vector(xShift, yShift)
        def newVector = vector.invertWay()

        then:
        newVector == new Vector(invertedXShift, invertedYShift)

        where:
        xShift | yShift | invertedXShift | invertedYShift
        0      | 0      | 0              | 0
        -1     | 0      | 1              | 0
        10     | -10    | -10            | 10
    }
    
    def 'area can be created width valid dimensions'() {
        when:
        def area = new Area(width, height)

        then:
        area.width == width
        area.height == height

        where:
        width | height
        1     | 1
        10    | 10
        2     | 5

    }

    def 'area cannot be created with invalid dimensions'() {
        when:
        new Area(width, height)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == expectedMessage

        where:
        width | height | expectedMessage
        0     | 0      | "'width=0' and should be greater than 0"
        0     | 10     | "'width=0' and should be greater than 0"
        10    | 0      | "'height=0' and should be greater than 0"
        -1    | 10     | "'width=-1' and should be greater than 0"
        10    | -1     | "'height=-1' and should be greater than 0"
    }

    def 'area determines when a point is inside or outside'() {
        given:
        def area = new Area(10, 10)
        def point = new PositivePoint(x, y)

        expect:
        area.contains(point) == expectedInsideness

        where:
        x  | y  | expectedInsideness
        0  | 0  | true
        5  | 5  | true
        10 | 0  | true
        0  | 10 | true
        10 | 10 | true
        0  | 11 | false
        11 | 0  | false
    }

}