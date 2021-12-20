import kotlin.math.abs

fun puzzleDayFifteenPartOne() {
    val inputs = readInput(15).toCavePositions()
    val best = searchOptimalPath(inputs, 20_000_000)!!.sumOf { it.risk } - inputs[0][0]
    println("Best way found in runs: $best")
}

fun List<String>.toCavePositions(): Array<Array<Int>> =
    map { it.map { char -> Integer.parseInt(char.toString()) }.toTypedArray() }.toTypedArray()

fun searchOptimalPath(
    map: Array<Array<Int>>,
    runs: Int = 2_000_000
): List<CavePosition>? {
    val destination = CavePosition(map.lastIndex, map.first().lastIndex, map.last().last())
    val start = CavePosition(0, 0, map[0][0])
    val bestForPosition: Array<Array<Int?>> = (1..map.size).map {
        arrayOfNulls<Int?>(map.size)
    }.toTypedArray()
    val knownDeadEnds = mutableSetOf<Int>()

    var bestWay: List<CavePosition>? = null
    var counter = 0

    while (counter < runs) {
        val better = findWayFrom(
            current = start,
            destination = destination,
            map = map,
            bestForPosition = bestForPosition,
            visited = emptyList(),
            currentRisk = 0,
            knownDeadEnds = knownDeadEnds
        )

        val newRisk = better?.sumOf { it.risk } ?: Integer.MAX_VALUE
        val currentBestRisk = bestWay?.sumOf { it.risk } ?: Integer.MAX_VALUE

        if (newRisk < currentBestRisk) {
            bestWay = better
            println("better way found with risk ${newRisk - start.risk}")
        }
        counter++
    }

    return bestWay
}

tailrec fun findWayFrom(
    current: CavePosition,
    destination: CavePosition,
    map: Array<Array<Int>>,
    bestForPosition: Array<Array<Int?>>,
    visited: List<CavePosition>,
    currentRisk: Int,
    knownDeadEnds: MutableSet<Int>
): List<CavePosition>? {
    val updatedVisits = visited + current
    val updatedRisk = currentRisk + current.risk

    bestForPosition[current.y][current.x] = updatedRisk

    if (current == destination) {
        return updatedVisits
    }

    val visitable = visitablePositionsFrom(current, map).filterNot { candidate ->
        isCandidateUnworthy(candidate, updatedVisits, updatedRisk, knownDeadEnds, bestForPosition)
    }

    if (visitable.isEmpty()) {
        knownDeadEnds.add(updatedVisits.hashCode())
        return null
    }

    return findWayFrom(visitable.random(), destination, map, bestForPosition, updatedVisits, updatedRisk, knownDeadEnds)
}

fun isCandidateUnworthy(
    candidate: CavePosition,
    visited: List<CavePosition>,
    currentRisk: Int,
    knownDeadEnds: MutableSet<Int>,
    bestForPosition: Array<Array<Int?>>
): Boolean {
    val candidatePathRisk = currentRisk + candidate.risk
    val knownBest = bestForPosition[candidate.y][candidate.x]
    val hasBeenVisited =  visited.contains(candidate)
    val touchesPath = visited.count { candidate.inProximityTo(it) } > 1
    val hasBetterRouteTo = (knownBest != null && candidatePathRisk > knownBest)
    val isDeadEnd = knownDeadEnds.contains((visited + candidate).hashCode())
    return hasBeenVisited || touchesPath || hasBetterRouteTo || isDeadEnd
}

fun visitablePositionsFrom(
    current: CavePosition,
    map: Array<Array<Int>>
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
    }
}

data class CavePosition(val y: Int, val x: Int, val risk: Int) {
    fun inProximityTo(other: CavePosition): Boolean {
        val yDiff = abs(y - other.y)
        val xDiff = abs(x - other.x)
        return yDiff <= 1 && xDiff <= 1 && (xDiff + yDiff) < 2
    }
}
