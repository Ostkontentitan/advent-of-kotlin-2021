import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.math.abs

suspend fun puzzleDayFifteenPartOne() {
    val inputs = readInput(15).toCavePositions()
    val longestWay = bruteForce(CavePosition(99, 99, 5), inputs, 2_000_000, 994)
    println("Puzzle number 15 -> ${longestWay.totalRisk}")
}

fun List<String>.toCavePositions(): Array<Array<Int>> =
    map { it.map { char -> Integer.parseInt(char.toString()) }.toTypedArray() }.toTypedArray()

suspend fun bruteForce(
    destination: CavePosition,
    map: Array<Array<Int>>,
    runs: Int,
    minRisk: Int = Integer.MAX_VALUE
): PathFindingResult = withContext(Dispatchers.Default) {
    var runNumber = 0
    var best = PathFindingResult(emptyList(), Int.MAX_VALUE, false)
    var bestRisk = minRisk
    val routesToAvoid = mutableSetOf<Int>()

    do {
        runNumber++
        val latestAdditions =
            (1..8).map { async { findWayFrom(CavePosition(0, 0, 0), destination, map, emptyList(), 0, bestRisk, routesToAvoid) } }.awaitAll()

        latestAdditions.forEach {
            routesToAvoid.add(it.hashCode())
        }

        val bestAddition = latestAdditions.filter { it.complete }.minByOrNull { it.totalRisk }
        val risk = bestAddition?.totalRisk ?: Int.MAX_VALUE
        if (risk < bestRisk) {
            best = bestAddition!!
            bestRisk = risk
            println("Found quicker route with risk of: $risk")
        }
    } while (runNumber <= runs)

    best
}

tailrec fun findWayFrom(
    current: CavePosition,
    destination: CavePosition,
    map: Array<Array<Int>>,
    visited: List<CavePosition>,
    currentRisk: Int,
    bestRisk: Int,
    routesToAvoid: Set<Int>
): PathFindingResult {
    val updatedVisits = visited + current
    val newRisk = currentRisk + current.risk

    if (newRisk > bestRisk) {
        return PathFindingResult(updatedVisits, newRisk, false)
    }

    val visitable = visitablePositionsFrom(current, map, updatedVisits, routesToAvoid)

    if (visitable.contains(destination)) {
        return PathFindingResult(updatedVisits + destination, newRisk, true)
    }

    if (visitable.isEmpty()) {
        return PathFindingResult(updatedVisits, newRisk, false)
    }

    return findWayFrom(visitable.random(), destination, map, updatedVisits, newRisk, bestRisk, routesToAvoid)
}

fun visitablePositionsFrom(
    current: CavePosition,
    map: Array<Array<Int>>,
    visited: List<CavePosition>,
    routesToAvoid: Set<Int>,
): List<CavePosition> {
    val x = current.x
    val y = current.y
    return listOf(
        x + 1 to y,
        x to y + 1,
        x to y - 1,
        x - 1 to y
    ).mapNotNull { (x, y) ->
        val risk = map.getOrNull(x)?.getOrNull(y)
        if (risk == null) {
            null
        } else {
            CavePosition(x, y, risk)
        }
    }.filterNot { visitable ->
        visited.count { visitable.inProximityTo(it) } > 1 || routesToAvoid.contains((visited + visitable).hashCode())
    }
}

data class CavePosition(val x: Int, val y: Int, val risk: Int) {
    fun inProximityTo(other: CavePosition): Boolean = abs(x + y - (other.x + other.y)) < 2
}

data class PathFindingResult(val path: List<CavePosition>, val totalRisk: Int, val complete: Boolean)
