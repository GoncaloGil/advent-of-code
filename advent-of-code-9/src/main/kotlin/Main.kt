import java.io.File
import java.lang.Exception
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val inputStream = File("src/main/resources/puzzle.txt").inputStream().bufferedReader()
    val lines = inputStream.readLines()

    val rope = Rope(9)
    lines.forEach { rope.readPuzzleLine(it) }
    rope.ropeSections.forEachIndexed { index, ropeSection ->  println( "$index -  ${ropeSection.tail}") }
    println(rope.ropeSections.last().tailVisitedPositions.size)
}

class Rope(nSections: Int) {
    val ropeHead: Position = Position(0,0)
    val ropeSections: List<RopeSection> = MutableList(nSections) { RopeSection() }

    fun readPuzzleLine(puzzleLine: String) {
        val args = puzzleLine.split(" ")
        repeat(args[1].toInt()) {
            when(args[0]) {
                "U" -> ropeHead.move(Direction.UP)
                "D" -> ropeHead.move(Direction.DOWN)
                "L" -> ropeHead.move(Direction.LEFT)
                "R" -> ropeHead.move(Direction.RIGHT)
            }
            moveRope()
        }
    }

    fun moveRope() {
        var accumulatedPosition = ropeHead
        ropeSections.forEach {
            it.moveHeadTo(accumulatedPosition)
            accumulatedPosition = it.tail
        }
    }

    fun getRopeTailPosition(): Position {
        return ropeSections.last().tail
    }
}

class RopeSection() {
    var head = Position(0,0)
    val tail = Position(0,0)
    val tailVisitedPositions: HashSet<Position> = HashSet()

    init {
        tailVisitedPositions.add(Position(tail.x,tail.y))
    }

    fun adjustTail() {
        val distance = Position(head.x - tail.x, head.y-tail.y)
        if(distance.x.absoluteValue + distance.y.absoluteValue >= 3) {
            moveTailOnce(Position(distance.x,0))
            moveTailOnce(Position(0,distance.y))
            tailVisitedPositions.add(Position(tail.x,tail.y))
        } else if(distance.x.absoluteValue == 2 || distance.y.absoluteValue == 2) {
            moveTailOnce(distance)
            tailVisitedPositions.add(Position(tail.x,tail.y))
        }
    }

    private fun moveTailOnce(distance: Position) {
        when {
            distance.x > 0 -> tail.move(Direction.RIGHT)
            distance.x < 0 -> tail.move(Direction.LEFT)
            distance.y > 0 -> tail.move(Direction.UP)
            distance.y < 0 -> tail.move(Direction.DOWN)
        }
    }

    fun moveHeadTo(position: Position) {
        head = position
        adjustTail()
    }
}

data class Position(var x: Int, var y: Int) {
    fun move(direction: Direction) {
        when(direction) {
            Direction.UP -> y++
            Direction.DOWN -> y--
            Direction.LEFT -> x--
            Direction.RIGHT -> x++
        }
    }
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}