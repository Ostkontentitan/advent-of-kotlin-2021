fun puzzleDayNinePartOne() {
    val inputs = readInput(9).map { lines -> lines.map { Integer.parseInt(it.toString()) } }

    val bumpedSum = findLowPoints(inputs).map { it.height }.sumOf { it + 1 }
    println("Low point sum is: $bumpedSum")
}

fun puzzleDayNinePartTwo() {
    val inputs = readInput(9).map { lines -> lines.map { Integer.parseInt(it.toString()) } }
    val lowPoints = findLowPoints(inputs)
    val basins = detectBasinSizes(inputs, lowPoints)
    val sizesMultiplied = basins.sortedDescending().take(3).fold(0L) { acc, item ->
        if (acc == 0L) item.toLong() else item * acc
    }
    println("Multiplied basin size is: $sizesMultiplied")
}

fun detectBasinSizes(inputs: List<List<Int>>, lowPoints: List<FloorTile>): List<Int> =
    lowPoints.map { lowPoint ->
        var surroundings = getSurroundings(inputs, lowPoint)
        val results = (listOf(lowPoint) + surroundings).toMutableSet()

        while (surroundings.isNotEmpty()) {
            surroundings = surroundings.flatMap { tile ->
                getSurroundings(inputs, tile)
            }.filterNot { results.contains(it) }.toSet()
            results.addAll(surroundings)
            println("new surroundings addded: ${surroundings.size} - resultsize: ${results.size}")
        }

        results.size
    }

fun getSurroundings(inputs: List<List<Int>>, tile: FloorTile): Set<FloorTile> {
    val x = tile.posX
    val y = tile.posY

    val above = if (y != inputs.lastIndex) notNineOrNull(inputs, x, y + 1) else null
    val below = if (y != 0) notNineOrNull(inputs, x, y - 1) else null
    val left = if (x != 0) notNineOrNull(inputs, x - 1, y) else null
    val right = if (x != inputs[0].lastIndex) notNineOrNull(inputs, x + 1, y) else null

    return setOfNotNull(above, below, left, right)
}

fun notNineOrNull(inputs: List<List<Int>>, x: Int, y: Int): FloorTile? {
    val input = inputs[y][x]
    return if (input != 9) {
        FloorTile(x, y, input)
    } else null
}

fun findLowPoints(inputs: List<List<Int>>): List<FloorTile> {
    val lowPoints = mutableListOf<FloorTile>()

    (0..inputs.lastIndex).forEach { y ->
        (0..inputs[0].lastIndex).forEach { x ->
            val above = if (y != inputs.lastIndex) inputs[y + 1][x] else 10
            val below = if (y != 0) inputs[y - 1][x] else 10
            val left = if (x != 0) inputs[y][x - 1] else 10
            val right = if (x != inputs[0].lastIndex) inputs[y][x + 1] else 10
            val current = inputs[y][x]
            if (current < above && current < below && current < left && current < right) {
                lowPoints += FloorTile(x, y, current)
            }
        }
    }

    return lowPoints
}

data class FloorTile(val posX: Int, val posY: Int, val height: Int)
