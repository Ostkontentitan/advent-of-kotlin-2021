import kotlin.math.abs

fun puzzleDaySevenPartOne() {
    val input = readInput(7)[0]
    val initialPositions = parseCrabPositions(input)

    val distances = calculateFuelConsumptions(initialPositions) { target, position -> abs(position - target) }
    val (optimalPoint, leastFuelConsumption) = distances.first()
    println("Shortest fuel consumption of $leastFuelConsumption required for position $optimalPoint")
}

fun puzzleDaySevenPartTwo() {
    val input = readInput(7)[0]
    val initialPositions = parseCrabPositions(input)

    val distances = calculateFuelConsumptions(initialPositions) { target, position ->
        val diff = abs(position - target)
        (1..diff).sumOf { it }
    }
    val (optimalPoint, leastFuelConsumption) = distances.first()
    println("Adjusted optimal is $leastFuelConsumption matching position $optimalPoint")
}

fun calculateFuelConsumptions(positions: List<Int>, fuelCalculation: (Int, Int) -> (Int)): List<Pair<Int, Int>> {

    val sortedPositions = positions.sorted()
    val firstCrab = sortedPositions.first()
    val lastCrab = sortedPositions.last()

    return (firstCrab..lastCrab).map { target ->
        target to sortedPositions.sumOf { fuelCalculation(target, it) }
    }.sortedBy { it.second }
}

fun parseCrabPositions(input: String) = input.split(",").map { it.toInt() }
