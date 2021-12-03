import kotlin.math.abs

fun puzzleDayTwoPartOne() {
    val inputs = readInput(2)
    val position = inputs.fold(Position()) { acc, item ->
        val number = getNumber(item)
        when(getDirection(item)) {
            Direction.UP -> acc.copy(height = acc.height + number)
            Direction.DOWN -> acc.copy(height = acc.height - number)
            Direction.FORWARD -> acc.copy(distance = acc.distance + number)
        }
    }
    println(abs(position.height * position.distance))
}

fun puzzleDayTwoPartTwo() {
    val inputs = readInput(2)
    val navigationalState = inputs.fold(NavigationalState()) { acc, item ->
        val number = getNumber(item)
        when(getDirection(item)) {
            Direction.UP -> acc.copy(aim = acc.aim - number)
            Direction.DOWN -> acc.copy(aim = acc.aim + number)
            Direction.FORWARD -> acc.copy(distance = acc.distance + number, height = acc.height + acc.aim * number)
        }
    }
    println(abs(navigationalState.height * navigationalState.distance))
}


fun getDirection(input: String): Direction = when {
    input.startsWith("forward") -> Direction.FORWARD
    input.startsWith("up") -> Direction.UP
    input.startsWith("down") -> Direction.DOWN
    else -> throw IllegalArgumentException("Unhandled direction $input")
}

fun getNumber(input: String): Int {
    val numberIndex = input.indexOf(" ") + 1
    return input[numberIndex].toString().toInt()
}

data class Position(val height: Int = 0, val distance: Int = 0)
data class NavigationalState(val height: Int = 0, val distance: Int = 0, val aim: Int = 0)

enum class Direction {
    UP, DOWN, FORWARD;
}