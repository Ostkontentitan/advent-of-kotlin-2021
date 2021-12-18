import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.collections.LinkedHashSet
import kotlin.math.abs

suspend fun puzzleDayFifteenPartOne() {
    val inputs = readInput(15).toCavePositions()
    do {
        searchOptimalPath(inputs, 200_000, 976, true)
    } while (true)
}

fun List<String>.toCavePositions(): Array<Array<Int>> =
    map { it.map { char -> Integer.parseInt(char.toString()) }.toTypedArray() }.toTypedArray()

suspend fun searchOptimalPath(
    map: Array<Array<Int>>,
    runs: Int,
    minRisk: Int = Integer.MAX_VALUE,
    readWriteRouteHashes: Boolean = false
): PathFindingResult = withContext(Dispatchers.Default) {
    val destination = CavePosition(map.lastIndex, map.first().lastIndex, map.last().last())
    val start = CavePosition(0, 0, 0)
    var best = PathFindingResult(emptyList(), minRisk, false)

    println("Reading routes to avoid from file. ")
    val routesToAvoid: MutableSet<Int> =
        if (readWriteRouteHashes) readRouteHashesFromFile() else linkedSetOf()

    print("Searching for optimal path")
    val startTime = System.currentTimeMillis()
    repeat(runs) { runNumber ->
        if (runNumber % 20_000 == 0) {
            print(".")
        }
        val latestAdditions =
            (1..8).map {
                async {
                    findWayFrom(
                        current = start,
                        destination = destination,
                        map = map,
                        visited = listOf(),
                        currentRisk = 0,
                        bestRisk = best.totalRisk,
                        routesToAvoid = routesToAvoid
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
    }
    val searchTime = (System.currentTimeMillis() - startTime) / 1000L
    println()
    println("Finished in: $searchTime")
    println("Writing hashes of visited routes")

    if (readWriteRouteHashes) {
        writeRouteHashesIntoFile(routesToAvoid)
    }

    best
}

fun writeRouteHashesIntoFile(routesToAvoid: Set<Int>) {
    val size = routesToAvoid.size
    val startIndex = if (size > MAX_HASHES) {
        size - MAX_HASHES
    } else {
        0
    }
    File("routesToAvoid.txt").printWriter().use { out ->
        routesToAvoid.forEachIndexed { index, hash ->
            if (index > startIndex) {
                out.println("$hash")
            }
        }
    }
}

fun readRouteHashesFromFile(): MutableSet<Int> {
    val hashes = LinkedHashSet<Int>(MAX_HASHES + 20_000_000)
    File("routesToAvoid.txt").apply {
        if (!exists()) {
            createNewFile()
        }
    }.forEachLine {
        hashes.add(it.toInt())
    }
    return hashes
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
    routesToAvoid: Set<Int>
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

private const val MAX_HASHES = 50_000_000