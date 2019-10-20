package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

import static org.homyspace.mousegrid.Command.*

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialMap = new Map()
        def initialPosition = new PositivePoint(2, 2)
        def initialDirection = Direction.@N

        when:
        Mouse mouse = new Mouse(initialMap, initialPosition, initialDirection)

        then:
        mouse.broadcastPosition() == initialPosition
        mouse.broadcastDirection() == initialDirection
    }

    def 'complains if is NOT inside a map'() {

        given:
        def initialMap = new Map(10, 10)
        def initialPosition = new PositivePoint(20, 20)
        def initialDirection = Direction.@N

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
    def 'carry on move commands "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == position
        mouse.broadcastDirection() == direction

        where:
        commands | position                | direction
        "FFFB"   | new PositivePoint(0, 2) | Direction.@N
        "FFBB"   | new PositivePoint(0, 0) | Direction.@N

    }

    @Unroll
    def 'carry on turn commands "#commands", then new direction "#newDirection"'() {

        given:
        Mouse mouse = new Mouse(new Map(), new PositivePoint(0, 0), Direction.@N)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == new PositivePoint(0, 0)
        mouse.broadcastDirection() == newDirection

        where:
        commands | newDirection
        "LL"     | Direction.@S
        "LR"     | Direction.@N
        "RL"     | Direction.@N
        "RR"     | Direction.@S
        "LLL"    | Direction.@E
        "RRR"    | Direction.@W

    }

    @Unroll
    def 'carry on a mix of movement and turn commands "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == newPosition
        mouse.broadcastDirection() == newDirection

        where:
        commands  | newPosition             | newDirection
        "FRFR"    | new PositivePoint(1, 1) | Direction.@S
        "RFFFLFF" | new PositivePoint(3, 2) | Direction.@N

    }

}