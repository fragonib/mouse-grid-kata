# Mouse grid kata (eq. to rover kata)

## Objectives
This kata is intended to create an API that moves a mouse around on a grid.

## Description of kata
* Develop an API that moves a mouse around a grid.
* You will have to provide the map size
* You will have to provide a number of obstacles and their position
* You will have to provide the initial starting point (x,y) of a mouse and the direction (N,S,E,W) it is facing.
* The mouse receives a character array of commands.
* Implement commands that move the mouse forward/backward (F,B).
* Implement commands that turn the mouse left/right (L,R).
* Implement wrapping from one edge of the grid to another (mouse wheels are spheres after all)
* Implement obstacle detection before each move to a new square. If a given sequence of commands encounters an obstacle, the mouse moves up to the last possible point and reports the obstacle.

## Points to focus on
- Design patterns
- SOLID
- Testing

## Technologies used
- Gradle (for building project)
- Kotlin (for production code)
- Groovy (for Testing with Spock)
