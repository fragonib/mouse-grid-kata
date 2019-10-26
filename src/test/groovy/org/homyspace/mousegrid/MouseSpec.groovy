package org.homyspace.mousegrid

import spock.lang.Specification
import spock.lang.Unroll

class MouseSpec extends Specification {

    def 'has an initial position and direction'() {

        given:
        def initialGrid = new Grid()
        def initialPosition = new PositivePoint(2, 2)
        def initialDirection = Direction.@N

        when:
        Mouse mouse = new FreeMouse(initialGrid, initialPosition, initialDirection)

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
        new FreeMouse(initialGrid, initialPosition, initialDirection)

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
        new FreeMouse(initialGrid, initialPosition, initialDirection)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains("mouse 'position' should NOT be inside an obstacle")
    }

    @Unroll
    def 'receive valid commands "#commands"'() {

        given:
        Mouse mouse = new FreeMouse()

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
    def 'complains with invalid "#commands"'() {

        given:
        Mouse mouse = new FreeMouse()

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
    def 'carry on move commands "#commands"'() {

        given:
        Mouse mouse = new FreeMouse()

        when:
        mouse = mouse.executeCommands(commands)

        then:
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection

        where:
        commands | expectedPosition        | expectedDirection
        "FFFB"   | new PositivePoint(0, 2) | Direction.@N
        "FFBB"   | new PositivePoint(0, 0) | Direction.@N
    }

    @Unroll
    def 'carry on turn commands "#commands", then new direction "#newDirection"'() {

        given:
        def grid = new Grid()
        def initialPosition = new PositivePoint(0, 0)
        def initialDirection = Direction.@N
        Mouse mouse = new FreeMouse(grid, initialPosition, initialDirection)

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
    def 'carry on a mix of movement and turn commands "#commands"'() {

        given:
        def initialGrid = new Grid(5, 5)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new FreeMouse(initialGrid, initialPosition, initialDirection)

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
    def 'carry on commands "#commands" when obstacles on grid'() {

        given:
        def someObstacle = new Obstacle(2, 3)
        def initialGrid = new Grid(5, 5, someObstacle)
        def initialPosition = new PositivePoint()
        def initialDirection = Direction.@N
        Mouse mouse = new FreeMouse(initialGrid, initialPosition, initialDirection)

        when:
        mouse = mouse.executeCommands(commands)

        then:
        expectedMouseType.isCase(mouse)
        mouse.broadcastPosition() == expectedPosition
        mouse.broadcastDirection() == expectedDirection
        if (mouse instanceof BlockedMouse)
            assert ((BlockedMouse) mouse).broadcastObstacle() == someObstacle

        where:
        commands     | expectedPosition        | expectedDirection | expectedMouseType
        "FRFR"       | new PositivePoint(1, 1) | Direction.@S      | FreeMouse
        "RFFLFFFRFF" | new PositivePoint(2, 2) | Direction.@N      | BlockedMouse
        "FFFRFFFLFF" | new PositivePoint(1, 3) | Direction.@E      | BlockedMouse

    }

}