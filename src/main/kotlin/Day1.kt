fun puzzleDayOnePartOne() {
    val inputs = readInput(1)
    val numbers: List<Int> = inputs.map { it.toInt() }

    var previous = -1
    var count = 0
    for (number in numbers) {
        if (previous != -1) {
            if (number > previous) {
                count++
            }
        }
        previous = number
    }
    println("Puzzle 1 result is: $count")
}

fun puzzleDayOnePartTwo() {
    val inputs = readInput(1)
    val numbers: List<Int> = inputs.map { it.toInt() }.windowed(3).map { it.sum() }

    var previous = -1
    var count = 0
    for (number in numbers) {
        if (previous != -1) {
            if (number > previous) {
                count++
            }
        }
        previous = number
    }
    println("Puzzle 1 result is: $count")
}
