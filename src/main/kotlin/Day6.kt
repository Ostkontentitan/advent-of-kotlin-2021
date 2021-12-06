fun puzzleDaySixPartOne() {
    val input = readInput(6).first()
    val fishes = inputToPopulation(input)

    val fishPrediction = predictPopulation(fishes, 80)
    println("Fish prediction after 80 days: ${fishPrediction.size}")
}

fun puzzleDaySixPartTwo() {
    val input = readInput(6).first()
    val model = inputToPopulationModel(input)

    val fishPrediction = predictPopulationWithModel(model, 256)
    println("Fish prediction after 80 days: ${fishPrediction.values.sum()}")
}

fun inputToPopulation(input: String) = input.split(",").map { Lanternfish(it.toInt()) }

fun predictPopulation(initial: List<Lanternfish>, daysToPass: Int) = (1..daysToPass).fold(initial) { acc, _ ->
    acc.flatMap {
        if (it.spawnTimer == 0) {
            listOf(it.copy(spawnTimer = 6), Lanternfish())
        } else {
            listOf(it.copy(spawnTimer = it.spawnTimer - 1))
        }
    }
}

fun predictPopulationWithModel(model: PopulationModel, daysToPass: Int) = (1..daysToPass).fold(model) { acc, _ ->
    val spawnCount = acc[0] ?: 0 // add to 6 and 8 later
    mapOf(
        0 to (acc[1] ?: 0),
        1 to (acc[2] ?: 0),
        2 to (acc[3] ?: 0),
        3 to (acc[4] ?: 0),
        4 to (acc[5] ?: 0),
        5 to (acc[6] ?: 0),
        6 to (acc[7] ?: 0) + spawnCount,
        7 to (acc[8] ?: 0),
        8 to spawnCount,
    )
}

fun inputToPopulationModel(input: String): PopulationModel =
    input.split(",").groupingBy { it.toInt() }.eachCount().mapValues { entry -> entry.value.toLong() }

data class Lanternfish(val spawnTimer: Int = 8)

typealias PopulationModel = Map<Int, Long>