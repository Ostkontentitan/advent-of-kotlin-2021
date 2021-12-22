package day15

class BacktrackingPathfinder : Pathfinder {
    override fun searchOptimalPath(
        map: Array<Array<Int>>
    ): Int {
        val destination = CavePosition(map.lastIndex, map.first().lastIndex, map.last().last())
        val start = CavePosition(0, 0, map[0][0])
        val bestForPosition: Array<Array<Int?>> = (1..map.size).map {
            arrayOfNulls<Int?>(map.size)
        }.toTypedArray()

        return findBestPath(
            destination = destination,
            map = map,
            bestForPosition = bestForPosition
        ) - start.risk
    }

    private fun findBestPath(
        destination: CavePosition,
        map: Array<Array<Int>>,
        bestForPosition: Array<Array<Int?>>
    ): Int {
        val pathfinder = ArrayDeque<CavePosition>()
        pathfinder.add(CavePosition(0, 0, map[0][0]))
        var bestRiskToDestination = Integer.MAX_VALUE

        while (pathfinder.isNotEmpty()) {
            val currentItem = pathfinder.last()
            val currentPath = pathfinder.toList()
            val currentRisk = currentPath.sumOf { it.risk }

            if (currentItem == destination && currentRisk < bestRiskToDestination) {
                bestRiskToDestination = currentRisk
                println("Found better path with risk $bestRiskToDestination")
            }

            bestForPosition[currentItem.y][currentItem.x] = currentRisk

            val visitable = visitablePositionsFrom(pathfinder.last(), map).filterNot { candidate ->
                isCandidateUnworthy(
                    candidate,
                    currentPath,
                    currentRisk,
                    bestForPosition,
                    bestRiskToDestination
                )
            }

            if (visitable.isEmpty()) {
                pathfinder.removeLast()
            } else {
                pathfinder.addLast(visitable.first())
            }
        }

        return bestRiskToDestination
    }

    private fun isCandidateUnworthy(
        candidate: CavePosition,
        visited: List<CavePosition>,
        currentRisk: Int,
        bestForPosition: Array<Array<Int?>>,
        bestTotalRisk: Int
    ): Boolean {
        val candidatePathRisk = currentRisk + candidate.risk
        if (candidatePathRisk >= bestTotalRisk) {
            return true
        }
        val knownBest = bestForPosition[candidate.y][candidate.x]
        val hasBetterOrEqualRouteTo = knownBest != null && candidatePathRisk >= knownBest

        if (hasBetterOrEqualRouteTo) {
            return true
        }

        if (visited.contains(candidate)) {
            return true
        }

        if (visited.count { candidate.inProximityTo(it) } > 1) {
            return true
        }

        return false
    }

    private fun visitablePositionsFrom(
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
}