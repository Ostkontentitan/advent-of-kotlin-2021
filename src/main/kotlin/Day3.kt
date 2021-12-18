fun puzzleDayThreePartOne() {
    val inputs = readInput(3)

    val totalInputs = inputs.size
    val chunkSize = inputs[0].length

    val mostSignificant = (0 until chunkSize).joinToString(separator = "") { index ->
        val oneCount = inputs.map { it[index] }.count { char -> char == '1' }
        if (oneCount > (totalInputs / 2)) "1" else "0"
    }

    val leastSignificant = mostSignificant.swapBinary()

    val gamma = Integer.parseUnsignedInt(mostSignificant, 2)
    val epsilon = Integer.parseUnsignedInt(leastSignificant, 2)
    println(gamma * epsilon)
}

fun puzzleDayThreePartTwo() {
    val inputs = readInput(3)

    val oxygenBinary = inputs.foldDownBySignificance(false)
    val co2Binary = inputs.foldDownBySignificance(true)

    val oxygen = Integer.parseUnsignedInt(oxygenBinary, 2)
    val co2 = Integer.parseUnsignedInt(co2Binary, 2)
    println(oxygen * co2)
}

fun List<String>.foldDownBySignificance(byLeast: Boolean = false) = (0 until this[0].length).fold(this) { acc: List<String>, index: Int ->
    if (acc.size == 1) return@fold acc
    val oneCount = acc.map { it[index] }.count { char -> char == '1' }
    val zeroCount = acc.map { it[index] }.count { char -> char == '0' }

    val significanceBit = if (byLeast) {
        if (oneCount < zeroCount) '1' else '0'
    } else {
        if (oneCount >= zeroCount) '1' else '0'
    }

    acc.filter { it[index] == significanceBit }
}.first()

fun String.swapBinary() = map { if (it == '1') '0' else '1' }.joinToString(separator = "")
