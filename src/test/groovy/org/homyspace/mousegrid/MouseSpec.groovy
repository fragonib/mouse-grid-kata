package org.homyspace.mousegrid

import spock.lang.Specification

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialPosition = new Point(2, 2)
        def initialDirection = Direction.NORTH

        when:
        Mouse mouse = new Mouse(initialPosition, initialDirection)

        then:
        mouse.broadcastPosition() == initialPosition
        mouse.broadcastDirection() == initialDirection
    }

}