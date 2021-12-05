import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun puzzleDayFivePartOne() {
    val inputs = readInput(5).map { it.trim() }

    val lines = parseLines(inputs).filter { it.isVertical() || it.isHorizontal() }

    val allPoints = lines.fold(emptyList<Point>()) { acc, line ->
        acc + line.points()
    }

    val distinctPoints = allPoints.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
    println("Overlapping point count: ${distinctPoints.size}")
}

fun puzzleDayFivePartTwo() {
    val inputs = readInput(5).map { it.trim() }

    val lines = parseLines(inputs)

    val allPoints = lines.fold(emptyList<Point>()) { acc, line ->
        acc + line.points()
    }

    val distinctPoints = allPoints.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
    println("Overlapping point count: ${distinctPoints.size}")
}

fun parseLines(inputs: List<String>): List<Line> = inputs.map {
    val points = it.split(" -> ")
    val from = points[0].split(",")
    val to = points[1].split(",")
    Line(Point(from[0].toInt(), from[1].toInt()), Point(to[0].toInt(), to[1].toInt()))
}

fun Line.points(): List<Point> =
    if (isHorizontal()) {
        val start = min(from.x, to.x)
        val end = max(from.x, to.x)
        (start..end).map {
            Point(it, from.y)
        }
    } else if (isVertical()) {
        val start = min(from.y, to.y)
        val end = max(from.y, to.y)
        (start..end).map {
            Point(from.x, it)
        }
    } else if (isFortyFiveDegreeDiagonal()) {
        val startX = min(from.x, to.x)
        if (areXyOnOppositeCourse()) {

            val endY = max(from.y, to.y)

            val steps = abs(from.x - to.x)
            (0..steps).map {
                Point(startX + it, endY - it)
            }
        } else {

            val startY = min(from.y, to.y)

            val steps = abs(from.x - to.x)
            (0..steps).map {
                Point(startX + it, startY + it)
            }
        }
    } else {
        throw IllegalStateException("Not a linear point. ")
    }

fun Line.isHorizontal() = from.y == to.y
fun Line.isVertical() = from.x == to.x
fun Line.isFortyFiveDegreeDiagonal() = abs(from.x - to.x) == abs(from.y - to.y)
fun Line.areXyOnOppositeCourse() = from.x - to.x != from.y - to.y

data class Point(val x: Int, val y: Int)
data class Line(val from: Point, val to: Point)