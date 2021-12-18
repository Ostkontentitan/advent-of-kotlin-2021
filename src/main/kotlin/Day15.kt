import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.math.abs

suspend fun puzzleDayFifteenPartOne() {
    val inputs = readInput(15).toCavePositions()
    val longestWay = bruteForce(CavePosition(99, 99, 5), inputs, 2_000_000, 1100)
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
    val start = CavePosition(0,0, 0)
    var runNumber = 0
    var best = PathFindingResult(emptyList(), minRisk, false)
    val routesToAvoid = mutableSetOf<Int>()

    do {
        runNumber++
        val latestAdditions =
            (1..8).map {
                async {
                    findWayFrom(
                        start,
                        destination,
                        map,
                        emptyList(),
                        0,
                        best.totalRisk,
                        routesToAvoid
                    )
                }
            }.awaitAll()

        latestAdditions.forEach {
            routesToAvoid.add(it.hashCode())
        }

        val bestAddition = latestAdditions.filter { it.complete }.minByOrNull { it.totalRisk }
        if (bestAddition != null) {
            best = bestAddition
            println("Found quicker route with risk of: ${bestAddition.totalRisk}")
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

    if (newRisk >= bestRisk) {
        return PathFindingResult(updatedVisits, newRisk, false)
    } else if (current == destination) {
        return PathFindingResult(updatedVisits, newRisk, true)
    }

    val visitable = visitablePositionsFrom(current, map, updatedVisits, routesToAvoid)

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
        y + 1 to x,
        y to x + 1,
        y to x - 1,
        y - 1 to x
    ).mapNotNull { (y, x) ->
        val risk = map.getOrNull(y)?.getOrNull(x)
        if (risk == null) {
            null
        } else {
            CavePosition(y, x, risk)
        }
    }.filterNot { visitable ->
        visited.count { visitable.inProximityTo(it) } > 1 || routesToAvoid.contains((visited + visitable).hashCode())
    }
}

data class CavePosition(val y: Int, val x: Int, val risk: Int) {
    fun inProximityTo(other: CavePosition): Boolean = abs(y + x - (other.y + other.x)) < 2
}

data class PathFindingResult(val path: List<CavePosition>, val totalRisk: Int, val complete: Boolean)
