package day15

fun searchOptimalPath(
    map: Array<Array<Int>>,
    runs: Int = 2_000_000
): Int {
    val destination = CavePosition(map.lastIndex, map.first().lastIndex, map.last().last())
    val start = CavePosition(0, 0, map[0][0])
    val bestForPosition: Array<Array<Int?>> = (1..map.size).map {
        arrayOfNulls<Int?>(map.size)
    }.toTypedArray()
    val knownDeadEnds = mutableSetOf<Int>()

    var bestRisk: Int = Integer.MAX_VALUE
    var counter = 0

    while (counter < runs) {
        val better = tryFindBetterPath(
            current = start,
            destination = destination,
            map = map,
            bestForPosition = bestForPosition,
            visited = emptyList(),
            currentRisk = 0,
            knownDeadEnds = knownDeadEnds
        )

        val newRisk = better?.sumOf { it.risk } ?: Integer.MAX_VALUE

        if (newRisk < bestRisk) {
            bestRisk = newRisk
            println("better way found with risk ${newRisk - start.risk}")
        }
        counter++
    }

    return bestRisk - start.risk
}

tailrec fun tryFindBetterPath(
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

    return tryFindBetterPath(
        visitable.random(),
        destination,
        map,
        bestForPosition,
        updatedVisits,
        updatedRisk,
        knownDeadEnds
    )
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
    val candidateRouteHash = (visited + candidate).hashCode()
    val hasBetterRouteTo = knownBest != null && candidatePathRisk > knownBest

    if (!hasBetterRouteTo) {
        bestForPosition[candidate.y][candidate.x] = candidatePathRisk
    }

    val hasBeenVisited = visited.contains(candidate)
    val touchesPath = visited.count { candidate.inProximityTo(it) } > 1
    val isDeadEnd = knownDeadEnds.contains(candidateRouteHash)

    val isUnworthy = hasBeenVisited || touchesPath || hasBetterRouteTo || isDeadEnd

    if (isUnworthy) {
        knownDeadEnds.add(candidateRouteHash)
    }

    return isUnworthy
}

fun visitablePositionsFrom(
    current: CavePosition,
    map: Array<Array<Int>>
): List<CavePosition> {
    val x = current.x
    val y = current.y
    val pool = listOf(
        y + 1 to x,
        y to x + 1,
        y to x - 1,
        y - 1 to x
    )
    return pool.mapNotNull { (y, x) ->
        val risk = map.getOrNull(y)?.getOrNull(x)
        if (risk == null) {
            null
        } else {
            CavePosition(y, x, risk)
        }
    }
}
