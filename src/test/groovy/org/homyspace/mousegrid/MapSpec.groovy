package org.homyspace.mousegrid

import spock.lang.Specification

class MapSpec extends Specification {

    def 'should return its size'() {
        given:
        Map map = new Map(new Size(5, 10))

        when:
        Size mapSize = map.size()

        then:
        mapSize == new Size(5, 10)
    }

}
