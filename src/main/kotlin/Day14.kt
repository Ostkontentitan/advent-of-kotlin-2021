import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

suspend fun puzzleDayFourteenPartOne() {
    val inputs = readInput(14)
    val instructions = extractInstructionPairs(inputs)
    val pairs = extractPolyTemplate(inputs.first())
    val final = performReplacementStepsAlt(pairs, instructions, 10)
    val counts = countCharsIn(final).values.sorted()
    val sum = counts.maxOf { it } - counts.minOf { it }
    println("Sum after 10 steps: $sum")
}

suspend fun puzzleDayFourteenPartTwo() {
    val inputs = readInput(14)
    val instructions = extractInstructionPairs(inputs)
    val pairs = extractPolyTemplate(inputs.first())
    val final = performReplacementStepsAlt(pairs, instructions, 40)
    val counts = countCharsIn(final).values.sorted()
    val sum = counts.maxOf { it } - counts.minOf { it }
    println("Sum after 40 steps: $sum")
}

suspend fun countCharsIn(sequence: Flow<PolyLink>) = withContext(Dispatchers.Default) {
    var counter = 0L
    println("counting chars...")
    sequence.fold(mapOf<Char, Long>()) { acc, value ->
        if(counter % 5_000_000L == 0L) {
            println("${counter / 1000000L} million")
        }
        counter++
        val new = value.first
        acc + (new to (acc[new] ?: 0L) + 1L)
    }
}

tailrec fun performReplacementStepsAlt(
    polyChain: Flow<PolyLink>,
    instructions: PolyInsertions,
    steps: Int
): Flow<PolyLink> {
    return if (steps > 0) {
        performReplacementStepsAlt(performReplacementStep(polyChain, instructions), instructions, steps - 1)
    } else {
        polyChain
    }
}

@OptIn(FlowPreview::class)
fun performReplacementStep(polyChain: Flow<PolyLink>, instructions: PolyInsertions): Flow<PolyLink> =
    polyChain.map { xy -> performInsertion(xy, instructions) }.flattenConcat()

suspend fun performInsertion(link: PolyLink, insertions: PolyInsertions): Flow<PolyLink> = flow {
    val insertion = insertions[link]
    if (insertion != null) {
        emit(link.first to insertion)
        emit(insertion to link.second)
    } else {
        emit(link)
    }
}

fun extractInstructionPairs(inputs: List<String>): PolyInsertions = inputs.subList(2, inputs.size).associate {
    val (target, replace) = it.split(" -> ")
    (target[0] to target[1]) to replace[0]
}

fun extractPolyTemplate(input: String): Flow<PolyLink> = input.windowed(size = 2, partialWindows = true).map {
    it[0] to it.getOrNull(1)
}.asFlow()

typealias PolyLink = Pair<Char, Char?>
typealias PolyInsertions = Map<PolyLink, Char>