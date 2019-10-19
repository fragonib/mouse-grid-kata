package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

import static org.homyspace.mousegrid.Command.*

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialMap = new Map()
        def initialPosition = new Point(2, 2)
        def initialDirection = Direction.N

        when:
        Mouse mouse = new Mouse(initialMap, initialPosition, initialDirection)

        then:
        mouse.broadcastPosition() == initialPosition
        mouse.broadcastDirection() == initialDirection
    }

    def 'is inside a map'() {

        given:
        def initialMap = new Map(new Area(10, 10))
        def initialPosition = new Point(20, 20)
        def initialDirection = Direction.N

        when:
        new Mouse(initialMap, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should be inside map")
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

    @Unroll
    def 'implement move commands "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        def newRover = mouse.executeCommands(commands)

        then:
        newRover.broadcastPosition() == position
        newRover.broadcastDirection() == direction

        where:
        commands | position        | direction
        ""       | new Point(0, 0) | Direction.N
        "FFFB"   | new Point(0, 2) | Direction.N
        "FFBB"   | new Point(0, 0) | Direction.N

    }


}