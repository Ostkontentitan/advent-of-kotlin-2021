fun puzzleDayElevenPartOne() {
    val inputs = readInput(11)

    val octos = inputs.mapIndexed { y, octoList ->
        octoList.mapIndexed { x, octoChar ->
            Octopus(
                octoChar.toString().toInt(), x, y
            )
        }
    }

    (1..100).forEach { step ->
        octos.forEach { octoList ->
            octoList.forEach { octo ->
                octoCharger(step, octos, listOf(octo))
            }
        }
    }

    val flashes = octos.sumOf { it.sumOf { octo -> octo.history.size } }
    println("Octo flashes after 100 steps: $flashes")
}

fun puzzleDayElevenPartTwo() {
    val inputs = readInput(11)

    val octos = inputs.mapIndexed { y, octoList ->
        octoList.mapIndexed { x, octoChar ->
            Octopus(
                octoChar.toString().toInt(), x, y
            )
        }
    }

    val totalOctos = octos.sumOf { it.size }

    var step = 1
    var foundMegaFlash = false

    while (!foundMegaFlash) {
        octos.forEach { octoList ->
            octoList.forEach { octo ->
                octoCharger(step, octos, listOf(octo))
            }
        }
        val flashes = octos.sumOf { octoList -> octoList.map { if (it.history.contains(step)) 1 else 0 }.sum() }
        if (flashes == totalOctos) {
            foundMegaFlash = true
            println("All octos flashed at step: $step")
        }
        step++
    }
}

tailrec fun octoCharger(step: Int, octos: OctoList, octopus: List<Octopus>) {
    val flashedOcti = octopus.filter { it.increaseCharge(step) }.flatMap { getSurroundingOctos(octos, it) }

    if (flashedOcti.isEmpty()) return

    octoCharger(step, octos, flashedOcti)
}

fun getSurroundingOctos(octoList: OctoList, octopus: Octopus): Set<Octopus> {
    val x = octopus.posX
    val y = octopus.posY

    val above = octoList.get(x, y + 1)
    val aboveRight = octoList.get(x + 1, y + 1)
    val aboveLeft = octoList.get(x - 1, y + 1)
    val below = octoList.get(x, y - 1)
    val belowRight = octoList.get(x + 1, y - 1)
    val belowLeft = octoList.get(x - 1, y - 1)
    val left = octoList.get(x - 1, y)
    val right = octoList.get(x + 1, y)

    return setOfNotNull(above, aboveRight, aboveLeft, below, belowRight, belowLeft, left, right)
}

typealias OctoList = List<List<Octopus>>

fun OctoList.get(x: Int, y: Int) = if (x in 0..this[0].lastIndex && y in 0..this.lastIndex) {
    this[y][x]
} else null

data class Octopus(var charge: Int, val posX: Int, val posY: Int) {
    val history = mutableSetOf<Int>()
    fun increaseCharge(step: Int): Boolean {
        if (history.contains(step) && charge == 0) {
            return false
        }

        charge++
        return if (charge > 9) {
            history.add(step)
            charge = 0
            true
        } else false
    }
}
