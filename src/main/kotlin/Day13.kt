fun puzzleDayThirteenPartOne() {
    val inputs = readInput(13)
    val coordinates = extractCoordinates(inputs)
    val instructions = extractInstructions(inputs)
    val instruction = instructions.first() as Fold.X
    val folded = foldLeft(coordinates, instruction.value)

    println("Dots after first fold: ${folded.size}")


}

fun puzzleDayThirteenPartTwo() {
    val inputs = readInput(13)
    val coordinates = extractCoordinates(inputs)
    val instructions = extractInstructions(inputs)
    val foldedPaper = foldCompletely(coordinates, instructions)
    val printable = visualizePaperDots(foldedPaper)
    printable.forEach { println(it) }
}

fun visualizePaperDots(foldedPaper: Set<Point>): List<String> {
    val maxY = foldedPaper.maxOf { it.y }
    val maxX = foldedPaper.maxOf { it.x }
    val canvas = (0 .. maxY).map { ".".repeat(maxX + 1) }
    return foldedPaper.fold(canvas) { canv, point ->
        canv.mapIndexed { index, string ->
            if (index == point.y) {
                string.mapIndexed { index, c -> if (index == point.x) '#' else c }.joinToString("")
            } else string
        }
    }
}

fun foldCompletely(coordinates: Set<Point>, instructions: List<Fold>): Set<Point> =
    instructions.fold(coordinates) { paper, instruction ->
        return@fold when (instruction) {
            is Fold.X -> foldLeft(paper, instruction.value)
            is Fold.Y -> foldUp(paper, instruction.value)
        }
    }

fun extractInstructions(inputs: List<String>): List<Fold> {
    val slice = inputs.subList(inputs.indexOfFirst { it.startsWith("fold along ") }, inputs.size)
    return slice.map { it.replace("fold along ", "") }.map {
        val (xOrY, value) = it.split("=")
        when (xOrY) {
            "x" -> Fold.X(value.toInt())
            "y" -> Fold.Y(value.toInt())
            else -> throw IllegalArgumentException("only x or y expected")

        }
    }
}

sealed class Fold {
    abstract val value: Int

    data class X(override val value: Int) : Fold()
    data class Y(override val value: Int) : Fold()
}

fun extractCoordinates(inputs: List<String>): Set<Point> = inputs.subList(0, inputs.indexOfFirst { it.isEmpty() }).map {
    val (x, y) = it.split(",")
    Point(x.toInt(), y.toInt())
}.toSet()

fun foldUp(coordinates: Set<Point>, foldY: Int): Set<Point> {
    val matchingCoordinates = coordinates.filter { it.y > foldY }
    val newCoordinates = matchingCoordinates.mapNotNull {
        val newY = foldY - (it.y - foldY)
        if (newY >= 0) Point(it.x, newY) else null
    }
    return (coordinates.filter { it.y < foldY } + newCoordinates).toSet()
}

fun foldLeft(coordinates: Set<Point>, foldX: Int): Set<Point> {
    val matchingCoordinates = coordinates.filter { it.x > foldX }
    val newCoordinates = matchingCoordinates.mapNotNull {
        val newX = foldX - (it.x - foldX)
        if (newX >= 0) Point(newX, it.y) else null
    }
    return (coordinates.filter { it.x < foldX } + newCoordinates).toSet()
}


