fun puzzleDayTwelvePartOne() {
    val inputs = readInput(12)
    val connections = inputsToConnections(inputs)
    val pathes = findUniquePaths(Cave.Start, connections, mapOf(Cave.Start to 1))

    println("Number of pathes $pathes")
}

fun puzzleDayTwelvePartTwo() {
    val inputs = readInput(12)
    val connections = inputsToConnections(inputs)
    val pathes = findUniquePaths(Cave.Start, connections, mapOf(Cave.Start to 1), true)

    println("Number of pathes with 'golden ticket' $pathes")
}

fun inputsToConnections(inputs: List<String>): List<Connection> = inputs.map {
    val split = it.split("-")
    Connection(Cave(split[0]), Cave(split[1]))
}

fun findUniquePaths(from: Cave, connections: List<Connection>, visited: Map<Cave, Int>): Int {
    if (from == Cave.End) {
        return 1
    }
    val connectionsToElement = connections.filter { it.contains(from) }.filterVisitable(from, visited)
    val nextOnes = connectionsToElement.map { it.getOther(from) }

    val updatedVisits = visited.incrementedForItem(from)
    return nextOnes.sumOf { findUniquePaths(it, connections, updatedVisits) }
}

fun findUniquePaths(
    from: Cave,
    connections: List<Connection>,
    visited: Map<Cave, Int>,
    goldenTicketAvailable: Boolean
): Int {
    if (from == Cave.End) {
        return 1
    }
    val connectionsToElement = connections.filter { it.contains(from) }.filterVisitable(from, visited)
    val ticketConnections = if(goldenTicketAvailable) connections.filter { it.contains(from) } - connectionsToElement.toSet() else emptyList()

    val nextOnes = connectionsToElement.map { it.getOther(from) }.filterNot { it == Cave.Start }
    val nextOnesWithTicket = ticketConnections.map { it.getOther(from) }.filterNot { it.isBig || it == Cave.Start }

    val updatedVisits = visited.incrementedForItem(from)
    return nextOnes.sumOf { findUniquePaths(it, connections, updatedVisits, goldenTicketAvailable) } +
            nextOnesWithTicket.sumOf { findUniquePaths(it, connections, updatedVisits, false) }
}

fun List<Connection>.filterVisitable(cave: Cave, visited: Map<Cave, Int>) =
    this.filter { it.getOther(cave).isVisitable(visited) }

fun Cave.isVisitable(visited: Map<Cave, Int>) = (visited[this] ?: 0) < this.maxVisits

fun Map<Cave, Int>.incrementedForItem(item: Cave): Map<Cave, Int> =
    this + (item to (this[item] ?: 0) + 1)


data class Cave(val key: String) {
    val isSmall = key == key.lowercase()
    val isBig = !isSmall
    val maxVisits = if (isSmall) 1 else Int.MAX_VALUE

    companion object {
        val Start = Cave("start")
        val End = Cave("end")
    }
}

data class Connection(private val one: Cave, private val two: Cave) {
    fun contains(cave: Cave) = one == cave || two == cave
    fun getOther(cave: Cave) =
        if (cave == one) two else if (cave == two) one else throw IllegalArgumentException("Other call with non contained element. ")
}
