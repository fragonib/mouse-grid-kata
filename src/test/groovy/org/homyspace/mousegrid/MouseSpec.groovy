package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

import static org.homyspace.mousegrid.Command.*

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialMap = new Grid()
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
        def initialMap = new Grid(10, 10)
        def initialPosition = new PositivePoint(20, 20)
        def initialDirection = Direction.@N

        when:
        new Mouse(initialMap, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should be inside map")
    }

    def 'complains if is inside an obstacle'() {

        given:
        def initialMap = new Grid(10, 10, new Obstacle(2, 4))
        def initialPosition = new PositivePoint(2, 4)
        def initialDirection = Direction.@N

        when:
        new Mouse(initialMap, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should NOT be inside an obstacle")
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
        Mouse mouse = new Mouse(new Grid(), new PositivePoint(0, 0), Direction.@N)

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
        Grid initialMap = new Grid(5, 5)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new Mouse(initialMap, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands  | expectedPosition        | expectedDirection
        "FRFR"    | new PositivePoint(1, 1) | Direction.@S
        "RFFFLFF" | new PositivePoint(3, 2) | Direction.@N
        "RFFFLFF" | new PositivePoint(3, 2) | Direction.@N
        "RFFFLFF" | new PositivePoint(3, 2) | Direction.@N
        "FFFFF"   | new PositivePoint(0, 0) | Direction.@N
        "LFF"     | new PositivePoint(3, 0) | Direction.@W

    }

    @Unroll
    def 'carry on commands "#commands" with an obstacle on its way'() {

        given:
        Grid initialMap = new Grid(5, 5, new Obstacle(2, 3))
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new Mouse(initialMap, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands  | expectedPosition        | expectedDirection
        "FRFR"    | new PositivePoint(1, 1) | Direction.@S
        "RFFLFFF" | new PositivePoint(2, 2) | Direction.@N
        "FFFRFF"  | new PositivePoint(1, 3) | Direction.@E

    }

}