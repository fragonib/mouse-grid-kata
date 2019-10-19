package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

import static org.homyspace.mousegrid.Command.*

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

    @Unroll
    def 'receive valid commands "#commands"'() {

        when:
        Mouse mouse = new Mouse()

        then:
        mouse.receiveCommands(commands) == commandsRead

        where:
        commands | commandsRead
        ""       | [ ]
        "FBFB"   | [ F, B, F, B ]

    }

    @Unroll
    def 'complains with invalid "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        mouse.receiveCommands(commands)

        then:
        def error = thrown(expectedException)
        error.message.contains(expectedMessage)

        where:
        commands | expectedException        | expectedMessage
        "W"      | IllegalArgumentException | 'Command.W'
        "FFXF"   | IllegalArgumentException | 'Command.X'

    }

}