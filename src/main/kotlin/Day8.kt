fun puzzleDayEightPartOne() {
    val inputs = readInput(8)
    val structured = structureInputs(inputs)
    val count = structured.countOneFourSevenEight()
    print("Count of 1,4,7,8 -> $count")
}

fun puzzleDayEightPartTwo() {
    val inputs = readInput(8)
    val structured = structureInputs(inputs)
    val sum = structured.sumOf { pairsList ->
        val codes = decode(pairsList.first)
        pairsList.second.map {
            val result = codes[it.toSet()]
            if(result == null) {
                throw IllegalArgumentException("Could not find: $it in decoded set. ")
            }
            result
        }.joinToString(separator = "").toInt()
    }
    print("Sum of all outputs -> $sum")
}

fun List<Pair<List<String>, List<String>>>.countOneFourSevenEight() = flatMap {
    it.second
}.count { ONE_FOUR_SEVEN_EIGHT_LENGTHS.contains(it.length) }

fun structureInputs(inputs: List<String>): List<Pair<List<String>, List<String>>> = inputs.map { input ->
    val split = input.split(" | ")
    split[0].trim().split(" ") to split[1].trim().split(" ")
}

fun decode(codes: List<String>): Map<Set<Char>, Int> {
    val sorted = codes.sortedBy { it.length }
    val codeOne = sorted[0]
    val codeSeven = sorted[1]
    val codeFour = sorted[2]
    val codeEight = sorted.last()

    val charsByCount = sorted.joinToString().toList().groupingBy { it }.eachCount()

    val segmentF = charsByCount.filter { it.value == 9 }.map { it.key }.first()
    val codeTwo = sorted.first { !it.contains(segmentF) }

    val segmentC = codeOne.toSet().intersect(codeTwo.toSet()).first()
    val fiveAndSix = sorted.filter { !it.contains(segmentC) }.sortedBy { it.length }
    val codeFive = fiveAndSix.first()
    val codeSix = fiveAndSix.last()

    val segmentE = (codeSix.toSet() - codeFive.toSet()).first()

    val codeNine = codeEight.replace(segmentE.toString(), "")
    val codeZero = sorted.first { it.length == 6 && it.toSet() != codeNine.toSet() && it.toSet() != codeSix.toSet() }
    val codeThree = sorted.first { it.length == 5 && it.toSet() != codeTwo.toSet() && it.toSet() != codeFive.toSet() }

    return mapOf(
        codeZero.toSet() to 0,
        codeOne.toSet() to 1,
        codeTwo.toSet() to 2,
        codeThree.toSet() to 3,
        codeFour.toSet() to 4,
        codeFive.toSet() to 5,
        codeSix.toSet() to 6,
        codeSeven.toSet() to 7,
        codeEight.toSet() to 8,
        codeNine.toSet() to 9
    )
}

private val ONE_FOUR_SEVEN_EIGHT_LENGTHS = setOf(2, 3, 4, 7)
