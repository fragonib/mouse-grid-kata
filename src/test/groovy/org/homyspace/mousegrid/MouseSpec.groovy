package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

import static org.homyspace.mousegrid.Command.*

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialMap = new Map()
        def initialPosition = new Point(2, 2)
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
        def initialPosition = new Point(20, 20)
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
    def 'should obey move commands "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == position
        mouse.broadcastDirection() == direction

        where:
        commands | position        | direction
        "FFFB"   | new Point(0, 2) | Direction.@N
        "FFBB"   | new Point(0, 0) | Direction.@N

    }

    @Unroll
    def 'on turn commands "#commands", then new direction "#newDirection"'() {

        given:
        Mouse mouse = new Mouse(new Map(), new Point(0, 0), Direction.@N)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == new Point(0, 0)
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
    def 'mix movement and turn commands "#commands"'() {

        given:
        Mouse mouse = new Mouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == newPosition
        mouse.broadcastDirection() == newDirection

        where:
        commands  | newPosition     | newDirection
        "FRFR"    | new Point(1, 1) | Direction.@S
        "RFFFLFF" | new Point(3, 2) | Direction.@N

    }

}