package day15

import readInput

fun puzzle() {
    val inputs = readInput(15).toRiskMap()
    // val bestPartOne = RecursivePathfinder().searchOptimalPath(inputs)
    // println("Best way found in runs: $bestPartOne")

    // 4246
    val revealed = revealActualCave(inputs)
    val bestPartTwo = BacktrackingPathfinder().searchOptimalPath(revealed)
    println("Hardmode: Best way has risk: $bestPartTwo")
}

fun revealActualCave(incompleteMap: CaveRiskMap): CaveRiskMap {
    val size = incompleteMap.size * 5

    return (0 until size).map { y ->
        (0 until size).map { x ->

            val xShift = x / incompleteMap.size
            val yShift = y / incompleteMap.size

            val xMod = x % incompleteMap.size
            val yMod = y % incompleteMap.size

            val shift = xShift + yShift
            val baseVal = incompleteMap[yMod][xMod]
            val new = baseVal + shift
            if (new > 9) new - 9 else new
        }.toTypedArray()
    }.toTypedArray()
}

fun List<String>.toRiskMap(): Array<Array<Int>> =
    map { it.map { char -> Integer.parseInt(char.toString()) }.toTypedArray() }.toTypedArray()

typealias CaveRiskMap = Array<Array<Int>>
