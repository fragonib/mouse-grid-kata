package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

class MouseSpec extends Specification {

    def 'has given an initial position and direction'() {

        given:
        def initialGrid = new Grid()
        def initialPosition = new PositivePoint(2, 2)
        def initialDirection = Direction.@N

        when:
        Mouse mouse = new ReadyMouse(initialGrid, initialPosition, initialDirection)

        then:
        mouse.broadcastPosition() == initialPosition
        mouse.broadcastDirection() == initialDirection
    }

    def 'complains if is NOT inside grid'() {

        given:
        def initialGrid = new Grid(10, 10)
        def initialPosition = new PositivePoint(20, 20)
        def initialDirection = Direction.@N

        when:
        new ReadyMouse(initialGrid, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should be inside grid")
    }

    def 'complains if is inside an obstacle'() {

        given:
        def initialGrid = new Grid(10, 10, new Obstacle(2, 4))
        def initialPosition = new PositivePoint(2, 4)
        def initialDirection = Direction.@N

        when:
        new ReadyMouse(initialGrid, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should NOT be inside an obstacle")
    }

    @Unroll
    def 'receive valid commands "#commandSequence"'() {

        given:
        Mouse mouse = new ReadyMouse()

        when:
        def actions = mouse.receiveCommands(commandSequence)

        then:
        actions == expectedActions

        where:
        commandSequence | expectedActions
        ""              | [ ]
        "FB"            | [ MoveForward.INSTANCE, MoveBackwards.INSTANCE ]
        "LR"            | [ TurnLeft.INSTANCE, TurnRight.INSTANCE ]
    }

    @Unroll
    def 'complains with invalid commands "#commands"'() {

        given:
        Mouse mouse = new ReadyMouse()

        when:
        mouse.receiveCommands(commands)

        then:
        def error = thrown(expectedException)
        error.message.contains(expectedMessageSubstring)

        where:
        commands | expectedException        | expectedMessageSubstring
        "W"      | IllegalArgumentException | 'Command.W'
        "FFXF"   | IllegalArgumentException | 'Command.X'

    }

    @Unroll
    def 'execute move commands "#commands"'() {

        given:
        Mouse mouse = new ReadyMouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands | expectedPosition        | expectedDirection
        "FFFB"   | new PositivePoint(0, 2) | Direction.@N
        "FFBB"   | new PositivePoint(0, 0) | Direction.@N
        "BBB"    | new PositivePoint(0, 7) | Direction.@N
    }

    @Unroll
    def 'execute turn commands "#commands", then new direction "#expectedDirection"'() {

        given:
        def grid = new Grid()
        def initialPosition = new PositivePoint(0, 0)
        def initialDirection = Direction.@N
        Mouse mouse = new ReadyMouse(grid, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == initialPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands | expectedDirection
        "LL"     | Direction.@S
        "LR"     | Direction.@N
        "RL"     | Direction.@N
        "RR"     | Direction.@S
        "LLL"    | Direction.@E
        "RRR"    | Direction.@W

    }

    @Unroll
    def 'execute a mix of movement and turn commands "#commands"'() {

        given:
        def initialGrid = new Grid(5, 5)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new ReadyMouse(initialGrid, initialPosition, initialDirection)

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
    def 'execute all commands "#commands" when #scenario'() {

        given:
        def someObstacle = new Obstacle(2, 3)
        def initialGrid = new Grid(5, 5, someObstacle)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new ReadyMouse(initialGrid, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse instanceof ReadyMouse
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands         | expectedPosition        | expectedDirection | scenario
        "FRFR"           | new PositivePoint(1, 1) | Direction.@S      | 'passing far from obstacle'
        "FFFRF"          | new PositivePoint(1, 3) | Direction.@E      | 'next to obstacle'
        "RFFLFFRFLFFLFR" | new PositivePoint(2, 4) | Direction.@N      | 'moving around obstacle'

    }

    @Unroll
    def 'execute commands "#commands" before collision with obstacle when #scenario'() {

        given:
        def someObstacle = new Obstacle(2, 3)
        def initialGrid = new Grid(5, 5, someObstacle)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new ReadyMouse(initialGrid, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse instanceof BlockedMouse
        mouse.broadcastPosition() == expectedLastPosition
        mouse.broadcastDirection() == expectedDirection
        ((BlockedMouse) mouse).broadcastObstacle() == someObstacle

        where:
        commands     | expectedLastPosition    | expectedDirection | scenario
        "RFFLFFFRFF" | new PositivePoint(2, 2) | Direction.@N      | 'colliding from left'
        "FFFRFFFLFF" | new PositivePoint(1, 3) | Direction.@E      | 'colliding from bottom'

    }

}